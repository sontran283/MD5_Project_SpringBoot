package com.ra.controller.admin;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.OrderRequestDTO;
import com.ra.model.dto.response.OrderResponseDTO;
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
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    // index
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDTO>> getListOrders() {
        List<OrderResponseDTO> ordersDTOList = ordersService.findAll();
        return new ResponseEntity<>(ordersDTOList, HttpStatus.OK);
    }

    // add order
    @PostMapping("/orders")
    public ResponseEntity<OrderResponseDTO> createOrders(@RequestBody OrderRequestDTO orderRequestDTO) throws CustomException {
        OrderResponseDTO newOrders = ordersService.saveOrUpdate(orderRequestDTO);
        return new ResponseEntity<>(newOrders, HttpStatus.CREATED);
    }

    // delete order
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        if (ordersService.findById(id) != null) {
            ordersService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<?> edit(@PathVariable("id") Long id) {
        OrderResponseDTO idEdit = ordersService.findById(id);
        if (idEdit != null) {
            return new ResponseEntity<>(idEdit, HttpStatus.OK);
        }
        return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/orders/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable("id") Long id, @RequestBody OrderRequestDTO orderRequestDTO) throws CustomException {
        OrderResponseDTO ordersDTO1 = ordersService.findById(id);
        if (ordersDTO1 != null) {
            ordersDTO1.setStatus(orderRequestDTO.getStatus());
            ordersService.saveOrUpdate(orderRequestDTO);
            return new ResponseEntity<>("Status updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/orders/search+sort+pagination")
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
