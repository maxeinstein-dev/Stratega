package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.AddGroupExpenseRequest;
import br.com.maxsueleinstein.stratega.domain.model.ExpenseGroup;

public interface AddGroupExpenseUseCase {
    ExpenseGroup execute(AddGroupExpenseRequest request);
}
