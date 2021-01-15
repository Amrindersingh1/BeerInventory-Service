package com.amrindersingh.beer.inventory.service.services;

import com.amrindersingh.beer.inventory.service.config.JmsConfig;
import com.amrindersingh.beer.inventory.service.domain.BeerInventory;
import com.amrindersingh.beer.inventory.service.repositories.BeerInventoryRepository;
import com.common.events.NewInventoryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service
public class NewInventoryListener {

    private final BeerInventoryRepository beerInventoryRepository;

    @Transactional
    @JmsListener(destination = JmsConfig.NEW_INVENTORY_QUEUE)
    public void listen(NewInventoryEvent event) {

        log.debug("Got Inventory: "+event.toString());
        beerInventoryRepository.save(BeerInventory.builder()
                .beerId(event.getBeerDto().getId())
                .upc(event.getBeerDto().getUpc())
                .quantityOnHand(event.getBeerDto().getQuantityOnHand())
                .build()
        );

    }

}
