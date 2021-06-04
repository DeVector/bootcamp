package com.project.bootcamp.service;

import com.project.bootcamp.exceptions.BusinessExceptions;
import com.project.bootcamp.exceptions.NotFoundExceptions;
import com.project.bootcamp.mapper.StockMapper;
import com.project.bootcamp.model.Stock;
import com.project.bootcamp.model.dto.StockDTO;
import com.project.bootcamp.repository.StockRepository;
import com.project.bootcamp.util.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockMapper stockMapper;

    @Transactional
    public StockDTO save(StockDTO dto) {

        Optional<Stock> optionalStock = stockRepository.findByNameAndDate(dto.getName(), dto.getDate());
        if(optionalStock.isPresent()){
            throw new BusinessExceptions(MessageUtils.STOCK_ALREADY_EXISTS);
        }

        Stock stock = stockMapper.toEntity(dto);
        stockRepository.save(stock);
        return stockMapper.toDto(stock);
    }

    @Transactional
    public StockDTO update(StockDTO dto) {
        Optional<Stock> optionalUpdate = stockRepository.findByStockUpdate(dto.getName(), dto.getDate(), dto.getId());
        if(optionalUpdate.isPresent()){
            throw new BusinessExceptions(MessageUtils.STOCK_ALREADY_EXISTS);
        }
        Stock stock = stockMapper.toEntity(dto);
        stockRepository.save(stock);
        return stockMapper.toDto(stock);
    }

    @Transactional
    public StockDTO delete(Long id) {
        StockDTO dto = this.findById(id);
        stockRepository.deleteById(dto.getId());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<StockDTO> findAll() {
        return stockMapper.toDtoList(stockRepository.findAll());
    }

    @Transactional(readOnly = true)
    public StockDTO findById(Long id) {
        return stockRepository.findById(id).map(stockMapper::toDto).orElseThrow(NotFoundExceptions::new);
    }


    @Transactional(readOnly = true)
    public List<StockDTO> findByToday() {
        return stockRepository.findByToday(LocalDate.now()).map(stockMapper::toDtoList).orElseThrow(NotFoundExceptions::new);
    }
}
