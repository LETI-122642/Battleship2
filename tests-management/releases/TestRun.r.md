# TestRun

## Testing Checklists

@LETI-110917

### S1 Relatórios
* [passed] C1 Gerar e validar relatórios Tags: reports
    tags: checklists, test-suite, unit-tests, reports
    meta: ID=TS-CHECK-001, TMS-ID=TMS-TS-CHECK-001, Executor=LETI-111610, Related Suite=TMS-TS-UNIT-001
    * Step 1 Gerar relatório de cobertura (HTML)
    * Step 2 Validar relatório HTML e anexar logs


### S2 Testes Automáticos - Unitários
* [passed] C2 Executar suites JUnit Tags: unit-tests
    tags: checklists, test-suite, unit-tests, reports
    meta: ID=TS-CHECK-001, TMS-ID=TMS-TS-CHECK-001, Executor=LETI-111610, Related Suite=TMS-TS-UNIT-001
    * Step 1 Executar `AllUnitTestsSuite` (ou suites específicas) via `mvn test`
    * Step 2 Recolher resultados, atualizar `releases/Test Run Unit Tests.t.md` e anexar logs


## Unit tests

### S3 Ships test case
* [passed] C3 BargeTest Tags: #ship-tests [TMS-ID: TMS-TC-BARGE-001]
    tags: #unit-tests, #test-cases
    meta: ID=TS-UNIT-002, TMS-ID=TMS-TS-UNIT-002, Executor=LETI-111610, Related Suite=TMS-TS-UNIT-001

* [passed] C4 CaravelTest Tags: #ship-tests [TMS-ID: TMS-TC-CARAVEL-001]
    tags: #unit-tests, #test-cases
    meta: ID=TS-UNIT-002, TMS-ID=TMS-TS-UNIT-002, Executor=LETI-111610, Related Suite=TMS-TS-UNIT-001

* [passed] C5 CarrackTest Tags: #ship-tests [TMS-ID: TMS-TC-CARRACK-001]
    tags: #unit-tests, #test-cases
    meta: ID=TS-UNIT-002, TMS-ID=TMS-TS-UNIT-002, Executor=LETI-111610, Related Suite=TMS-TS-UNIT-001

* [passed] C6 FrigateTest Tags: #ship-tests [TMS-ID: TMS-TC-FRIGATE-001]
    tags: #unit-tests, #test-cases
    meta: ID=TS-UNIT-002, TMS-ID=TMS-TS-UNIT-002, Executor=LETI-111610, Related Suite=TMS-TS-UNIT-001

* [passed] C7 GalleonTest Tags: #ship-tests [TMS-ID: TMS-TC-GALLEON-001]
    tags: #unit-tests, #test-cases
    meta: ID=TS-UNIT-002, TMS-ID=TMS-TS-UNIT-002, Executor=LETI-111610, Related Suite=TMS-TS-UNIT-001


### S4 Game test case
* [passed] C8 GameTest Tags: #game-tests [TMS-ID: TMS-TC-GAME-001]
    tags: #unit-tests, #test-cases
    meta: ID=TS-UNIT-002, TMS-ID=TMS-TS-UNIT-002, Executor=LETI-111610, Related Suite=TMS-TS-UNIT-001
    Notes: 

* [passed] Cada caso deve corresponder a uma classe de teste em `src/test/java/...`. Atualize os `TMS-ID` se já existirem IDs no projeto.
    tags: #unit-tests, #test-cases
    meta: ID=TS-UNIT-002, TMS-ID=TMS-TS-UNIT-002, Executor=LETI-111610, Related Suite=TMS-TS-UNIT-001
    

