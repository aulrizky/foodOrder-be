package com.example.demo.services;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

import com.example.demo.dto.CartDto;
import com.example.demo.dto.FoodCategoryDto;
import com.example.demo.dto.OrderDetailDto;
import com.example.demo.dto.OrderDto;
import com.example.demo.dto.PaginationDto;
import com.example.demo.dto.request.CartRequest;
import com.example.demo.dto.request.CheckoutCartIdRequest;
import com.example.demo.dto.request.CheckoutCartRequest;
import com.example.demo.dto.request.FoodListRequestDto;
import com.example.demo.dto.response.FoodListResponseDto;
import com.example.demo.dto.response.FoodResponse;
import com.example.demo.dto.response.HistoryCartResponse;
import com.example.demo.dto.request.PageRequest;
import com.example.demo.dto.request.PutQtyRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.CartResponse;
import com.example.demo.dto.response.FoodDetailResponse;
import com.example.demo.dto.response.FoodListDetailDto;
import com.example.demo.dto.response.FoodListResponse;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.dto.response.MyCartResponse;
import com.example.demo.dto.response.OrderHistoryResponse;
import com.example.demo.model.Favfoods;
import com.example.demo.model.Foods;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.Orders;
import com.example.demo.model.Users;
import com.example.demo.repositories.CartRepository;
import com.example.demo.repositories.FavfoodRepository;
import com.example.demo.repositories.FoodRepository;
import com.example.demo.repositories.OrderDetailRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.jwt.JwtUtil;
import com.example.demo.services.specification.FoodSpecFavorite;
import com.example.demo.services.specification.FoodSpecification;

import io.micrometer.core.ipc.http.HttpSender.Response;

import com.example.demo.model.Carts;
import com.example.demo.repositories.OrderRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FoodService {
  @Autowired
  private Validator validator;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private FoodRepository foodRepository;

  @Autowired
  private FavfoodRepository favoriteFoodRepository;

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private MessageSource messageSource;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OrderDetailRepository orderDetailRepository;

  public ResponseEntity<FoodListResponse> getFoods(FoodListRequestDto foodListRequestDto, Pageable page) {
    try {
      Specification<Foods> foodSpec = FoodSpecification.foodFilter(foodListRequestDto);
      Page<Foods> foods = foodRepository.findAll(foodSpec, page);

      int userId = JwtUtil.getCurrentUser().getUserId();

      List<FoodListResponseDto> foodsDto = foods.stream()
          .map(food -> new FoodListResponseDto(
              food.getFoodId(),
              new FoodCategoryDto(food.getCategories().getCategoryId(), food.getCategories().getCategoryName()),
              food.getFoodName(),
              food.getPrice(),
              food.getImageFilename(),
              getIsFav(food.getFoodId(), userId),
              getIsCartDelete(food.getFoodId(), userId)))
          .collect(Collectors.toList());

      long totalData = foods.getTotalElements();
      String message = messageSource.getMessage("get.foods.success", null, Locale.getDefault());

      return ResponseEntity.ok()
          .body(new FoodListResponse(totalData, foodsDto, message, HttpStatus.OK.value(),
              HttpStatus.OK.getReasonPhrase()));

    } catch (Exception e) {
      log.error(null, e);
      String message = messageSource.getMessage("internal.error", null, Locale.getDefault());
      return ResponseEntity
          .internalServerError()
          .body(new FoodListResponse(0, null, message, HttpStatus.INTERNAL_SERVER_ERROR.value(),
              HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }
  }

  public ResponseEntity<FoodListResponse> toggleFavorite(Integer foodId) {
    try {
      int userId = JwtUtil.getCurrentUser().getUserId();
      if (foodId == null) {
        String message = messageSource.getMessage("food.not.found", null, Locale.getDefault());
        return ResponseEntity.badRequest()
            .body(new FoodListResponse(0, null, message, HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_GATEWAY.getReasonPhrase()));
      }
      Optional<Foods> foodOptional = foodRepository.findById(foodId);
      if (!foodOptional.isPresent()) {
        String message = messageSource.getMessage("food.not.found", null, Locale.getDefault());
        return ResponseEntity.badRequest()
            .body(new FoodListResponse(0, null, message, HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_GATEWAY.getReasonPhrase()));
      }
      Optional<Favfoods> optionalFavoriteFood = favoriteFoodRepository.findFavoriteFoodByAndUser(userId, foodId);
      if (optionalFavoriteFood.isPresent()) {
        Favfoods favoriteFood = optionalFavoriteFood.get();
        favoriteFood.setIsFavorite(!favoriteFood.getIsFavorite());
        favoriteFoodRepository.save(favoriteFood);
      } else {
        Favfoods newFavfoods = Favfoods.builder()
            .foods(foodRepository.findById(foodId).orElseThrow())
            .users(userRepository.findById(userId).orElseThrow())
            .isFavorite(true)
            .build();
        favoriteFoodRepository.save(newFavfoods);

      }
      String message = messageSource.getMessage(getIsFav(foodId, userId) ? "add.favorite" : "remove.favorite", null,
          Locale.getDefault());

      String formatMessage = MessageFormat.format(message, foodOptional.get().getFoodName());

      return ResponseEntity
          .ok()
          .body(new FoodListResponse(1, null, formatMessage, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));

    } catch (Exception e) {
      log.error(null, e);
      String message = messageSource.getMessage("internal.error", null, Locale.getDefault());
      return ResponseEntity
          .internalServerError()
          .body(new FoodListResponse(0, null, message, HttpStatus.INTERNAL_SERVER_ERROR.value(),
              HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }
  }

  public ResponseEntity<CartResponse> addToCart(CartRequest request) {
    try {
      int userId = JwtUtil.getCurrentUser().getUserId();
      if (request.getFoodId() == null) {
        String message = messageSource.getMessage("food.not.found", null, Locale.getDefault());
        return ResponseEntity.badRequest()
            .body(new CartResponse(0, null, message, HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_GATEWAY.getReasonPhrase()));
      }
      Optional<Foods> foodOptional = foodRepository.findById(request.getFoodId());
      if (!foodOptional.isPresent()) {
        String message = messageSource.getMessage("food.not.found", null, Locale.getDefault());
        return ResponseEntity.badRequest()
            .body(new CartResponse(0, null, message, HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_GATEWAY.getReasonPhrase()));
      }
      Foods food = foodOptional.get();

      Carts cart = Carts.builder()
          .foods(food)
          .users(Users.builder().userId(userId).build())
          .qty(1).build();

      cartRepository.save(cart);
      FoodListResponseDto foodDto = new FoodListResponseDto(
          food.getFoodId(),
          new FoodCategoryDto(food.getCategories().getCategoryId(), food.getCategories().getCategoryName()),
          food.getFoodName(),
          food.getPrice(),
          food.getImageFilename(),
          getIsFav(food.getFoodId(), userId),
          getIsCartDelete(food.getFoodId(), userId));
      String message = messageSource.getMessage("add.to.cart.success", null, Locale.getDefault());

      String formatMessage = MessageFormat.format(message, foodOptional.get().getFoodName());

      return ResponseEntity
          .ok()
          .body(new CartResponse(1, foodDto, formatMessage, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));

    } catch (Exception e) {
      log.error(null, e);
      String message = messageSource.getMessage("internal.error", null, Locale.getDefault());
      return ResponseEntity
          .internalServerError()
          .body(new CartResponse(0, null, message, HttpStatus.INTERNAL_SERVER_ERROR.value(),
              HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }

  }

  public ResponseEntity<CartResponse> deleteCart(Integer foodId) {
    try {
      if (foodId == null) {
        String message = messageSource.getMessage("food.not.found", null, Locale.getDefault());
        return ResponseEntity.badRequest()
            .body(new CartResponse(0, null, message, HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_GATEWAY.getReasonPhrase()));
      }
      int userId = JwtUtil.getCurrentUser().getUserId();
      System.out.println(userId);
      System.out.println(foodId);
      Optional<Carts> OptionalCarts = cartRepository.findCartByFoodAndUser(userId, foodId);
      if (!OptionalCarts.isPresent()) {
        String message = messageSource.getMessage("cart.not.found", null, Locale.getDefault());
        return ResponseEntity.badRequest()
            .body(new CartResponse(0, null, message, HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_GATEWAY.getReasonPhrase()));
      }
      cartRepository.delete(OptionalCarts.get());
      Foods food = OptionalCarts.get().getFoods();
      FoodListResponseDto foodDto = new FoodListResponseDto(
          food.getFoodId(),
          new FoodCategoryDto(food.getCategories().getCategoryId(), food.getCategories().getCategoryName()),
          food.getFoodName(),
          food.getPrice(),
          food.getImageFilename(),
          getIsFav(food.getFoodId(), userId),
          getIsCartDelete(food.getFoodId(), userId));

      String message = messageSource.getMessage("delete.cart.success", null, Locale.getDefault());

      String formatMessage = MessageFormat.format(message, food.getFoodName());

      return ResponseEntity
          .ok()
          .body(new CartResponse(1, foodDto, formatMessage, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));

    } catch (Exception e) {
      log.error(null, e);
      String message = messageSource.getMessage("internal.error", null, Locale.getDefault());
      return ResponseEntity
          .internalServerError()
          .body(new CartResponse(0, null, message, HttpStatus.INTERNAL_SERVER_ERROR.value(),
              HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }
  }

  public ResponseEntity<FoodDetailResponse> getFoodsById(Integer foodId) {
    try {
      Optional<Foods> foodOptional = foodRepository.findById(foodId);
      if (!foodOptional.isPresent()) {
        String message = messageSource.getMessage("cart.not.found", null, Locale.getDefault());
        return ResponseEntity.badRequest()
            .body(new FoodDetailResponse(null, message, HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_GATEWAY.getReasonPhrase()));
      }
      int userId = JwtUtil.getCurrentUser().getUserId();
      Foods food = foodOptional.get();

      FoodListDetailDto foodDto = new FoodListDetailDto(
          food.getFoodId(),
          new FoodCategoryDto(food.getCategories().getCategoryId(), food.getCategories().getCategoryName()),
          food.getFoodName(),
          food.getPrice(),
          food.getImageFilename(),
          getIsFav(food.getFoodId(), userId),
          getIsCartDelete(food.getFoodId(), userId),
          food.getIngredient());

      String message = messageSource.getMessage("get.foods.success", null, Locale.getDefault());

      return ResponseEntity.ok()
          .body(new FoodDetailResponse(foodDto, message, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));

    } catch (Exception e) {
      log.error(null, e);
      String message = messageSource.getMessage("internal.error", null, Locale.getDefault());
      return ResponseEntity
          .internalServerError()
          .body(new FoodDetailResponse(null, message, HttpStatus.INTERNAL_SERVER_ERROR.value(),
              HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }
  }

  public ResponseEntity<OrderHistoryResponse> getHistoryOrderResponse(Pageable page) {
    Integer user = JwtUtil.getCurrentUser().getUserId();
    System.out.println(user);
    Page<Orders> ordersUser = orderRepository.findByUserId(user, page);
    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    List<OrderDto> orders = ordersUser.stream().map(order -> {
      LocalDateTime orderDate = convertToLocalDateTimeViaMilisecond(order.getOrderDate());
      return new OrderDto(
          order.getOrderId(),
          order.getTotalItem(),
          orderDate.format(myFormatObj),
          order.getTotalOrderPrice(),
          getOrderDetail(order.getOrderId()));
    }).collect(Collectors.toList());

    String message = messageSource.getMessage("get.history.success", null, Locale.getDefault());

    long totalData = orders.size();
    System.out.println(totalData);
    PaginationDto pagination = new PaginationDto(
        ordersUser.getNumber(),
        ordersUser.getTotalPages(),
        ordersUser.getSize(),
        ordersUser.getNumber() * ordersUser.getSize(),
        Math.min((ordersUser.getNumber() + 1) * ordersUser.getSize(), (int) ordersUser.getTotalElements()));
    return ResponseEntity
        .ok()
        .body(new OrderHistoryResponse(totalData, orders, message, HttpStatus.OK.value(),
            HttpStatus.OK.getReasonPhrase(), pagination));
  }

  public ResponseEntity<MyCartResponse> myChart() {
    int userId = JwtUtil.getCurrentUser().getUserId();
    List<Carts> chartsOptional = cartRepository.findByUserIdandIsDelete(userId);
    // int foodID = chartsOptional.get().getFoods().getFoodId();
    // Carts cart = chartsOptional.get();
    List<CartDto> carts = chartsOptional.stream().map(cart -> new CartDto(
        cart.getCartId(),
        cart.getUsers().getUserId(),
        cart.getUsers().getUsername(),
        cart.getQty(),
        cart.isDeleted(),
        cart.getCreatedBy(),
        cart.getCreatedTime(),
        cart.getModifiedBy(),
        cart.getModified_time(),
        getCartFoods(cart.getFoods().getFoodId(), userId))).collect(Collectors.toList());

    String message = messageSource.getMessage("get.mycart.success", null, Locale.getDefault());

    return ResponseEntity
        .ok()
        .body(new MyCartResponse(carts, message, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
  }

  public ResponseEntity<MessageResponse> putQtyCart(PutQtyRequest request, int cartId) {
    try {
      if (request.getQty() < 1) {
        Optional<Carts> chartOptional = cartRepository.findById(cartId);
        deleteCart(chartOptional.get().getFoods().getFoodId());
      }
      int userId = JwtUtil.getCurrentUser().getUserId();
      Optional<Carts> chartOptional = cartRepository.findCartByUserIdAndChartId(userId, cartId);
      if (!chartOptional.isPresent()) {
        String message = messageSource.getMessage("cart.not.found", null, Locale.getDefault());
        return ResponseEntity.badRequest()
            .body(
                new MessageResponse(message, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_GATEWAY.getReasonPhrase()));
      }

      Carts chart = chartOptional.get();

      chart.setQty(request.getQty());

      cartRepository.save(chart);
      String message = messageSource.getMessage("quantity.has.changed.success", null, Locale.getDefault());

      return ResponseEntity.ok()
          .body(new MessageResponse(message, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));

    } catch (Exception e) {
      log.error(null, e);
      String message = messageSource.getMessage("internal.error", null, Locale.getDefault());
      return ResponseEntity
          .internalServerError()
          .body(new MessageResponse(message, HttpStatus.INTERNAL_SERVER_ERROR.value(),
              HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }

  }

  @Transactional
  public ResponseEntity<MessageResponse> checkoutCart(List<CheckoutCartIdRequest> request) {
    try {
      int userId = JwtUtil.getCurrentUser().getUserId();
      long millis = System.currentTimeMillis();
      Date date = new java.sql.Date(millis);
      String user = JwtUtil.getCurrentUser().getUsername();
      // Users user = JwtUtil.getCurrentUser();
      Users userget = getUsers(userId);
      int totalOrderPrice = 0;
      int totalItem = 0;
      for (CheckoutCartIdRequest i : request) {
        System.out.println(i.getCartId());
      }
      // List<CheckoutCartIdRequest> cartIdRequests = request.getRequest();

      if (request.isEmpty()) {
        String message = messageSource.getMessage("empty.chart.error", null,
            Locale.getDefault());
        return ResponseEntity.badRequest()
            .body(
                new MessageResponse(message, HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_GATEWAY.getReasonPhrase()));
      }
      ;

      for (CheckoutCartIdRequest checkoutCartRequest : request) {
        Optional<Carts> postOder = cartRepository.findCartByUserIdAndChartId(userId,
            checkoutCartRequest.getCartId());
        if (!postOder.isPresent()) {
          String message = messageSource.getMessage("cart.not.found", null,
              Locale.getDefault());
          return ResponseEntity.badRequest()
              .body(new MessageResponse(message, HttpStatus.BAD_REQUEST.value(),
                  HttpStatus.BAD_GATEWAY.getReasonPhrase()));
        }
        Carts cart = postOder.get();
        int qty = cart.getQty();
        totalItem += cart.getQty();
        if (totalItem == 0) {
          String message = messageSource.getMessage("all.quantity.is.zero.error", null,
              Locale.getDefault());
          return ResponseEntity.badRequest()
              .body(new MessageResponse(message, HttpStatus.BAD_REQUEST.value(),
                  HttpStatus.BAD_GATEWAY.getReasonPhrase()));
        }
        totalOrderPrice += (cart.getFoods().getPrice() * qty);
      }
      ;

      Orders createOrder = Orders.builder()
          .user(userget)
          .orderDate(date)
          .totalItem(totalItem)
          .totalOrderPrice(totalOrderPrice)
          .createdBy(user)
          .createdTime(new Timestamp(System.currentTimeMillis()))
          .modifiedBy(user)
          .modified_time(new Timestamp(System.currentTimeMillis()))
          .build();
      Orders savedOrder = orderRepository.save(createOrder);

      for (CheckoutCartIdRequest checkoutCartRequestDetail : request) {
        Optional<Carts> carts = cartRepository.findCartByUserIdAndChartId(userId,
            checkoutCartRequestDetail.getCartId());
        if (!carts.isPresent()) {
          String message = messageSource.getMessage("cart.not.found", null,
              Locale.getDefault());
          return ResponseEntity.badRequest()
              .body(new MessageResponse(message, HttpStatus.BAD_REQUEST.value(),
                  HttpStatus.BAD_GATEWAY.getReasonPhrase()));
        }
        int qty = carts.get().getQty();
        if (qty == 0) {
          String message = messageSource.getMessage("quantity.is.zero.error", null,
              Locale.getDefault());
          return ResponseEntity.badRequest()
              .body(new MessageResponse(message, HttpStatus.BAD_REQUEST.value(),
                  HttpStatus.BAD_GATEWAY.getReasonPhrase()));
        }
        Carts cart = carts.get();

        OrderDetail createOrderDetail = OrderDetail.builder()
            .food(cart.getFoods())
            .order(savedOrder)
            .qty(cart.getQty())
            .totalPrice(cart.getQty() * cart.getFoods().getPrice())
            .createdBy(user)
            .createdTime(new Timestamp(System.currentTimeMillis()))
            .modifiedBy(user)
            .modified_time(new Timestamp(System.currentTimeMillis()))
            .Carts(cart)
            .build();

        orderDetailRepository.save(createOrderDetail);
        cart.setDeleted(true);
        cartRepository.save(cart);

      }
      String message = messageSource.getMessage("checkout.chart.success", null, Locale.getDefault());

      return ResponseEntity.ok()
          .body(new MessageResponse(message, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));

    } catch (Exception e) {
      log.error(null, e);
      String message = messageSource.getMessage("internal.error", null, Locale.getDefault());
      return ResponseEntity
          .internalServerError()
          .body(new MessageResponse(message, HttpStatus.INTERNAL_SERVER_ERROR.value(),
              HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }
  }

  public ResponseEntity<FoodListResponse> getMyFavFoods(FoodListRequestDto foodListRequestDto, Pageable page) {
    int userId = JwtUtil.getCurrentUser().getUserId();
    Specification<Favfoods> favSpec = FoodSpecFavorite.favFoodFilter(foodListRequestDto, userId);
    Page<Favfoods> foodList = favoriteFoodRepository.findAll(favSpec, page);

    // Page<Favfoods> foodList =
    // favoriteFoodRepository.findFavoriteFoodByUser(userId, page);
    List<FoodListResponseDto> foodsDto = foodList.stream()
        .map(food -> new FoodListResponseDto(
            food.getFoods().getFoodId(),
            new FoodCategoryDto(food.getFoods().getCategories().getCategoryId(),
                food.getFoods().getCategories().getCategoryName()),
            food.getFoods().getFoodName(),
            food.getFoods().getPrice(),
            food.getFoods().getImageFilename(),
            getIsFav(food.getFoods().getFoodId(), userId),
            getIsCartDelete(food.getFoods().getFoodId(), userId)))
        .collect(Collectors.toList());

    long totalData = foodList.getTotalElements();
    String message = messageSource.getMessage("get.foods.success", null, Locale.getDefault());

    return ResponseEntity.ok()
        .body(
            new FoodListResponse(totalData, foodsDto, message, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
  }

  private Boolean getIsFav(Integer foodId, Integer userId) {
    Optional<Favfoods> favoriteFood = favoriteFoodRepository.findFavoriteFoodByAndUser(userId, foodId);
    if (favoriteFood.isPresent()) {
      return favoriteFood.get().getIsFavorite();
    } else {
      return false;
    }
  }

  private Boolean getIsCartDelete(Integer foodId, Integer userId) {
    Optional<Carts> cart = cartRepository.findCartByFoodAndUser(userId, foodId);
    // System.out.println(cart.get().getFoods().getFoodId());
    if (cart.isPresent()) {
      return true;
    } else {
      return false;
    }
  }

  private List<OrderDetailDto> getOrderDetail(Integer orderId) {
    List<OrderDetail> oderData = orderDetailRepository.findByOrderOrderId(orderId);
    List<OrderDetailDto> oderDetail = oderData.stream().map(detail -> new OrderDetailDto(detail.getOrderDetailId(),
        detail.getFood().getFoodName(),
        detail.getFood().getImageFilename(),
        detail.getQty(),
        detail.getFood().getPrice(),
        detail.getTotalPrice())).collect(Collectors.toList());

    return oderDetail;
  }

  private FoodListResponseDto getCartFoods(Integer foodId, Integer userId) {
    Optional<Foods> foodOptional = foodRepository.findById(foodId);
    Foods food = foodOptional.get();

    FoodListResponseDto foodsDto = new FoodListResponseDto(food.getFoodId(),
        new FoodCategoryDto(food.getCategories().getCategoryId(), food.getCategories().getCategoryName()),
        food.getFoodName(),
        food.getPrice(),
        food.getImageFilename(),
        getIsFav(food.getFoodId(), userId),
        getIsCartDelete(food.getFoodId(), userId));

    return foodsDto;

  }

  private Users getUsers(Integer userId) {
    Optional<Users> usersOptional = userRepository.findById(userId);
    Users users = usersOptional.get();
    return users;
  }

  public LocalDateTime convertToLocalDateTimeViaMilisecond(Date dateToConvert) {
    return Instant.ofEpochMilli(dateToConvert.getTime())
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
  }
}
