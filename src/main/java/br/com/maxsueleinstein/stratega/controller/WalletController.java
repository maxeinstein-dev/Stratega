package br.com.maxsueleinstein.stratega.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallets")
public class WalletController {
    @PostMapping
    public void createWallets() {

    }

    @GetMapping
    public void getWallets() {

    }
}
