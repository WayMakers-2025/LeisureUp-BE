package org.leisureup.travel.internal.travel.controller;

import lombok.RequiredArgsConstructor;
import org.leisureup.travel.internal.travel.domain.Travel;
import org.leisureup.travel.internal.travel.service.TravelService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TravelController {

    private final TravelService travelService;
}
