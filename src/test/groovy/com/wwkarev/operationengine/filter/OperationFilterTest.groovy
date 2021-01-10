package com.wwkarev.operationengine.filter

import com.wwkarev.operationengine.Operation
import com.wwkarev.operationengine.condition.FalseCondition
import com.wwkarev.operationengine.condition.TrueCondition
import spock.lang.Shared
import spock.lang.Specification

class OperationFilterTest extends Specification {

    @Shared
    Operation<Void> operation1 = new Operation<Void>(UUID.randomUUID().toString(), new TrueCondition(UUID.randomUUID().toString()), null)

    @Shared
    Operation<Void> operation2 = new Operation<Void>(UUID.randomUUID().toString(), new FalseCondition(UUID.randomUUID().toString()), null)

    @Shared
    Operation<Void> operation3 = new Operation<Void>(UUID.randomUUID().toString(), new TrueCondition(UUID.randomUUID().toString()), null)

    @Shared
    Operation<Void> operation4 = new Operation<Void>(UUID.randomUUID().toString(), new FalseCondition(UUID.randomUUID().toString()), null)

    def "test LastValidOperationFilter"() {
        when:

        List<Operation<Void>> operations = [
                operation1,
                operation2,
                operation3,
                operation4
        ]

        List<Operation<Void>> filteredOperations = getOperationFilter(operations).filter()
        checkFunc(filteredOperations)
        then:
        notThrown Exception
        where:
        getOperationFilter | checkFunc
        {_operations -> return new LastValidOperationFilter(_operations) } | { _operationFilter -> checkFuncLast(_operationFilter) }
        {_operations -> return new FirstValidOperationFilter(_operations) } | { _operationFilter -> checkFuncFirst(_operationFilter) }
        {_operations -> return new AllValidOperationFilter(_operations) } | { _operationFilter -> checkFuncAll(_operationFilter) }
    }

    def checkFuncLast(List<Operation<Void>> operations) {
        assert operations.size() == 1
        operations[0].getId() == operation3.getId()
    }

    def checkFuncFirst(List<Operation<Void>> operations) {
        assert operations.size() == 1
        operations[0].getId() == operation1.getId()
    }

    def checkFuncAll(List<Operation<Void>> operations) {
        assert operations.size() == 2
        operations[0].getId() == operation1.getId()
        operations[1].getId() == operation3.getId()
    }

    def "test LastValidOperationFilter. No valid conditions"() {
        when:

        List<Operation<Void>> operations = [
                operation2,
                operation4
        ]

        getOperationFilter(operations).filter()
        then:
        thrown OperationFilter.OperationNotFound
        where:
        getOperationFilter | _
        {_operations -> return new LastValidOperationFilter(_operations) } | _
        {_operations -> return new FirstValidOperationFilter(_operations) } | _
        {_operations -> return new AllValidOperationFilter(_operations) } | _
    }
}
