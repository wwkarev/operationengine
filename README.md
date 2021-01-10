#### Operation engine

Operation engine allows structuring the invoking of actions and conditions under which these actions are invoked.

##### Operation

Operation contains Condition object and Action object.

##### Action

Action describes something that could happen in the application.

##### Condition

Condition tells the system whether the action should be performed or not.

##### OperationFilter

OperationFilter filters list of operations. For example: return first\last\all Operations with valid Condition.

##### Reducer

Reducer receives list of Operations and performs target task. 
For example: invoking all operation's actions.

##### Example #1

Suppose we have a task to buy home appliances. We can buy only one item, and we have a limited budget. 
Therefore, we have a set of conditions and positions in priority order.

This piece of code on spock lang simulates such task:


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

##### Example #2
Suppose we need to send an email with a list of customer names who have agreed to purchase our software.
In this situation Action would return customer name and reducer would combine it into one email.

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

#### Conditions

##### Construct condition
Condition object returns boolean value. Condition objects can be combined using the `&` (and), `|` (or), `~`(not) operators.
Target condition is a condition consists of subconditions such as AndCondition, OrCondition, NotCondition, CustomCondition.
It is possible to present via 'condition tree':


                      AndCondition
                /                    \
        OrCondition               NotCondition
        /         \                     |
    conditionXXX   conditionYYY     conditionZZZ

Constructing:
    
    Condition condition1 = new TrueCondition('conditionXXX')
    Condition condition2 = new TrueCondition('conditionYYY')
    Condition condition3 = new TrueCondition('conditionZZZ')
    Condition targetCondition = (condition1 | condition2) & !condition3

##### ConditionTransformer

ConditionTransformer transforms condition to list of literals.

    ConditionTransformer conditionTransformer = new ConditionTransformerToInfixLiteralsTest(targetCondition)
    List<Literal> transformedLiteralList = conditionTransformer.transform()

Transformation of target condition from previous example: targetCondition -> [OPEN_BRACKET, PARAM(conditionXXX), OR, PARAM(conditionYYY), CLOSE_BRACKET, AND, NOT, PARAM(conditionZZZ)].

##### ConditionCalculator

ConditionCalculator validates conditions and return Map - [key: id of condition, value: result of condition].

    ConditionCalculator conditionCalculator = new ConditionCalculator(condition)
    Map<String, Boolean> results = conditionCalculator.calculate()

##### MemorizingConditionTransformer
MemorizingConditionTransformer transforms condition (and all subconditions) to MemorizingCondition. 
It's useful using of MemorizingCondition with ConditionCalculator in order each condition is calculated only once. 

    MemorizingConditionTransformer conditionTransformer = new MemorizingConditionTransformer(condition)
    Condition memorizingCondition = conditionTransformer.transform()
    
##### LiteralNotationTransformer

LiteralNotationTransformer transforms list of literals from one notation to another.
For example: 

    LiteralNotationTransformer notationTransformer = new LiteralNotationTransformer()
    notationTransformer.infixToPrefix(literals)
    
Transforms `[OPEN_BRACKET, PARAM(conditionXXX), OR, PARAM(conditionYYY), CLOSE_BRACKET, AND, NOT, PARAM(conditionZZZ)]` -> `[AND, OR, PARAM(conditionXXX), PARAM(conditionYYY), NOT, PARAM(conditionZZZ)].`

##### ConditionConstructor

ConditionConstructor allows you to create Condition objects from strings. Consider the mechanism of such creation on the example of a logical expression:

`(conditionXXX or conditionYYY) and not conditionZZZ`
1. Build literal list 
2. Transform literal list to prefix notation
3. Construct condition

###### Build literal list
Logical expression is transformed to literal list.

(conditionXXX or conditionYYY) and not conditionZZZ -> [OPEN_BRACKET, PARAM(conditionXXX), OR, PARAM(conditionYYY), CLOSE_BRACKET, AND, NOT, PARAM(conditionZZZ)]

    OperatorRepresentation operatorRepresentation = new ClassicOperatorRepresentation()
    String logicalExpression = '(conditionXXX or conditionYYY) and not conditionZZZ'
    List<Literal> literals = new LiteralBuilder(operatorRepresentation).buildByLogicalExpression(logicalExpression)


Literal is an object with id (in case of PARAM will be equal to string representation 'conditionXXX' or 'conditionYYY') and literal type.
Possible LiteralTypes: PARAM, AND, OR, NOT, OPEN_BRACKET, CLOSE_BRACKET.
OperatorRepresentation - mapping object of operator, and it's string representation. 
Classic:
    AND - and
    OR - or
    NOT - not
Symbol:
    AND - &&
    OR - ||
    NOT - !
It is possible to define custom OperatorRepresentation.

###### Full condition construction code:
    
    OperatorRepresentation operatorRepresentation = new ClassicOperatorRepresentation()
    String logicalExpression = '(conditionXXX or conditionYYY) and not conditionZZZ'
    List<Literal> literals = new LiteralBuilder(operatorRepresentation).buildByLogicalExpression(logicalExpression)
    
    Condition condition1 = new TrueCondition('conditionXXX')
    Condition condition2 = new TrueCondition('conditionYYY')
    Condition condition3 = new TrueCondition('conditionZZZ')
    Map<String, Condition> conditions = [
                                            (condition1.getId()): condition1, 
                                            (condition2.getId()): condition2, 
                                            (condition3.getId()): condition3
                                        ]
    Condition targetCondition = new ConditionConstructorFromInfixLiterals(conditions, literals).construct()

