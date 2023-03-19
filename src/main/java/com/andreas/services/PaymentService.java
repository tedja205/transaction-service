package com.andreas.services;

import com.andreas.models.Constant;
import com.andreas.models.PaymentRequest;
import com.andreas.models.Wallet;
import com.andreas.repositories.WalletRepository;
import com.andreas.response.BaseResponse;
import com.andreas.response.CheckBalanceResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.message.ObjectArrayMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PaymentService extends BaseResponse {
    @Override
    public BaseResponse setBaseResponse(boolean success, List<?> data, String messageTitle, String message, List<String> validationMessage) {
        return super.setBaseResponse(success, data, messageTitle, message, validationMessage);
    }

    @Autowired
    private WalletRepository walletRepository;


    public ResponseEntity transaction(PaymentRequest paymentRequest, HttpServletRequest request, String transactionType) {
        List<String> validationMessage = new ArrayList<>();
        BigDecimal requestAmount = paymentRequest.getAmount();
        try {
            Wallet wallet = walletRepository.findWalletByUser_Username(request.getRemoteUser());

            if (wallet.getId() == null) {
                validationMessage.add("Wallet not found");
                BaseResponse response = setBaseResponse(false, new ArrayList<>(), "Failed " + transactionType.toLowerCase() + "!", "", validationMessage);
                return ResponseEntity.badRequest().body(response);
            }

            boolean isValid = validation(request, validationMessage, requestAmount, wallet.getBalance(), transactionType);
            if (!isValid) {
                BaseResponse response = setBaseResponse(false, new ArrayList<>(), "Failed " + transactionType.toLowerCase() + "!", "", validationMessage);
                return ResponseEntity.badRequest().body(response);
            }

            if (transactionType.equals(Constant.WITHDRAW)) {
                wallet.setBalance(minBalance(requestAmount, wallet.getBalance()));
            } else {
                wallet.setBalance(addBalance(requestAmount, wallet.getBalance()));
            }

            walletRepository.save(wallet);


        } catch (Exception exception) {
            validationMessage.add(exception.getMessage());
            BaseResponse response = setBaseResponse(false, new ArrayList<>(), transactionType.toUpperCase(), "Failed " + transactionType.toLowerCase() + "!", validationMessage);
            return ResponseEntity.badRequest().body(response);

        }
        BaseResponse response = setBaseResponse(true, new ArrayList<>(), transactionType.toUpperCase() , "Success " + transactionType.toLowerCase() + "!", null);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity checkBalance(HttpServletRequest request) {
        List<String> validationMessage = new ArrayList<>();
        List<CheckBalanceResponse> data = new ArrayList<>();

        try {
            Wallet wallet = walletRepository.findWalletByUser_Username(request.getRemoteUser());
            data.add(new CheckBalanceResponse(wallet.getBalance()));


        } catch (Exception exception) {
            validationMessage.add(exception.getMessage());
            BaseResponse response = setBaseResponse(false, new ArrayList<>(), "Check Balance", "Failed check balance", validationMessage);
            return ResponseEntity.badRequest().body(response);

        }
        BaseResponse response = setBaseResponse(true, data, "Check Balance", "Success check balance", null);

        return ResponseEntity.ok(response);
    }

    private BigDecimal addBalance(BigDecimal amount, BigDecimal currentBalance) {
        return currentBalance.add(amount);
    }

    private BigDecimal minBalance(BigDecimal amount, BigDecimal currentBalance) {
        return currentBalance.subtract(amount);
    }

    private boolean validation(HttpServletRequest httpServletRequest, List<String> validationMessage, BigDecimal amount, BigDecimal currentBalance, String transactionType) {
        Boolean isValid = true;
        if (!validateNumberFormat(amount)) {
            validationMessage.add("Invalid format amount");
            isValid = false;
        }
        if (transactionType.equals(Constant.WITHDRAW)) {
            if (isBalanceNotEnough(amount, currentBalance)) {
                validationMessage.add("Balance is not enough");
                isValid = false;
            }
        }

        return isValid;
    }

    private boolean isBalanceNotEnough(BigDecimal amount, BigDecimal currentBalance) {
        if (currentBalance.subtract(amount).compareTo(BigDecimal.valueOf(10000)) < 0) {
            return true;
        }
        return false;
    }

    private boolean validateNumberFormat(BigDecimal amount) {
        boolean isValid = true;

        String amountString = "0";
        if (amount != null) {
            amountString = amount.toString();
        }

        if (!(amountString.matches("[0-9]+") && amount.compareTo(new BigDecimal(0)) > 0)) {
            isValid = false;
        }
        if (amount.compareTo(BigDecimal.valueOf(10000)) < 0) {
            isValid = false;
        }

        return isValid;
    }


}
