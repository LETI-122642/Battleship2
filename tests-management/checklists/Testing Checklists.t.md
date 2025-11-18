# Testing Checklists
Tags: checklists, test-suite, unit-tests, reports
Meta: ID = TS-CHECK-001, TMS-ID = TMS-TS-CHECK-001, Executor = LETI-111610, Related Suite = TMS-TS-UNIT-001

## S1 Relatórios
* C1 Gerar e validar relatórios Tags: reports
    * Step 1 Gerar relatório de cobertura (HTML)
    * Step 2 Validar relatório HTML e anexar logs

## S2 Testes Automáticos - Unitários
* C2 Executar suites JUnit Tags: unit-tests
    * Step 1 Executar `AllUnitTestsSuite` (ou suites específicas) via `mvn test`
    * Step 2 Recolher resultados, atualizar `releases/Test Run Unit Tests.t.md` e anexar logs
