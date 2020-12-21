package com.wwkarev.operationengine

import com.wwkarev.operationengine.action.Action
import com.wwkarev.operationengine.condition.FalseCondition
import com.wwkarev.operationengine.condition.TrueCondition
import com.wwkarev.operationengine.filter.AllValidOperationFilter
import com.wwkarev.operationengine.filter.FirstValidOperationFilter
import com.wwkarev.operationengine.filter.OperationFilter
import com.wwkarev.operationengine.reducer.ActionInvoker
import com.wwkarev.operationengine.reducer.Reducer
import spock.lang.Specification

class OperationExecutorTest extends Specification {
    static class PurchaseAction<T> implements Action<T> {
        private String name

        PurchaseAction(String name) {
            this.name = name
        }

        @Override
        T invoke() {
            throw new Exception(name)
        }
    }

    def "test OperationExecutor. Purchases."() {
        when:
        List<Operation<Void>> operations = [
                new Operation<Void>(new FalseCondition(), new PurchaseAction('TV')),
                new Operation<Void>(new TrueCondition(), new PurchaseAction('VHSPlayer')),
                new Operation<Void>(new TrueCondition(), new PurchaseAction('Laptop'))
        ]

        OperationFilter<Void> operationFilter = new FirstValidOperationFilter<>(operations)
        Reducer<Void> reducer = new ActionInvoker<>()
        OperationExecutor<Void> operationExecutor = new OperationExecutor<>(reducer, operationFilter)
        String purchase = ''
        try {
            operationExecutor.execute()
        } catch (Exception e) {
            purchase = e.getMessage()
        }

        then:
        assert purchase == 'VHSPlayer'
    }

    static class EmailSenderReducer<T> implements Reducer<T> {
        @Override
        void reduce(List<Operation<T>> operations) {
            String emailBody = operations.collect{it.getAction().invoke()}.join(', ')
            throw new Exception(emailBody)
        }
    }

    static class CustomerNameAction<String> implements Action<String> {
        private String name

        CustomerNameAction(java.lang.String name) {
            this.name = name
        }

        @Override
        String invoke() {
            return name
        }
    }

    def "test OperationExecutor. Send email."() {
        when:
        List<Operation<Void>> operations = [
                new Operation<Void>(new TrueCondition(), new CustomerNameAction<String>('Michael Johnson')),
                new Operation<Void>(new FalseCondition(), new CustomerNameAction('Joe Thompson')),
                new Operation<Void>(new TrueCondition(), new CustomerNameAction('Betty Ross'))
        ]

        OperationFilter<Void> operationFilter = new AllValidOperationFilter<>(operations)
        Reducer<Void> reducer = new EmailSenderReducer<>()
        OperationExecutor<Void> operationExecutor = new OperationExecutor<>(reducer, operationFilter)
        String TARGET_NAMES = 'Michael Johnson, Betty Ross'
        String names = ''
        try {
            operationExecutor.execute()
        } catch (Exception e) {
            names = e.getMessage()
        }

        then:
        assert names == TARGET_NAMES
    }
}
