package com.ra.controller.admin;

import com.ra.exception.CustomException;
import com.ra.exception.OrderNotFoundException;
import com.ra.model.dto.response.OrderResponseDTO;
import com.ra.model.entity.Orders;
import com.ra.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    // index
    @GetMapping("/orders/index")
    public ResponseEntity<List<OrderResponseDTO>> index() {
        List<OrderResponseDTO> orderResponseDTO = ordersService.findAll();
        return new ResponseEntity<>(orderResponseDTO, HttpStatus.OK);
    }

    // orderById
    @GetMapping("orders/{id}")
    public ResponseEntity<?> orderById(@PathVariable("id") Long id) {
        OrderResponseDTO orderResponseDTO = ordersService.findById(id);
        if (orderResponseDTO != null) {
            return new ResponseEntity<>(orderResponseDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    // changeStatus
    @PatchMapping("/orders/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable("id") Long id, @RequestParam String status) throws CustomException, OrderNotFoundException {
        try {
            int orderStatus = Integer.parseInt(status);
            Long ChangeOrderId = Long.valueOf(id);
            if (orderStatus < 0 || orderStatus > 2) {
                throw new NumberFormatException("Invalid order status");
            }
            ordersService.changeStatus(ChangeOrderId, orderStatus);
            return new ResponseEntity<>("Status changed", HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }
    }

    // get ListOrderByStatus
    @GetMapping("/orders/ListOrderByStatus")
    public ResponseEntity<?> ListOrderByStatus(@RequestParam Integer status) {
        List<OrderResponseDTO> ordersList = ordersService.getListOrderByStatus(status);
        return new ResponseEntity<>(ordersList, HttpStatus.OK);
    }


    // search-sort-pagination
    @GetMapping("/orders/search-sort-pagination")
    public ResponseEntity<Page<OrderResponseDTO>> getOrders(
            @RequestParam(name = "search") Integer id,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "3") int size) {

        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }

        Page<OrderResponseDTO> ordersDTOPage = ordersService.searchOrdersById(pageable, id);
        return new ResponseEntity<>(ordersDTOPage, HttpStatus.OK);
    }
}
