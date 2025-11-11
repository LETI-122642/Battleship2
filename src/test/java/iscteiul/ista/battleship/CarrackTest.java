package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes parametrizados da classe Carrack (tamanho 3)")
class CarrackTest {

    @ParameterizedTest(name = "Carrack com direção {0} deve gerar três posições consecutivas corretas")
    @ValueSource(strings = {"NORTH", "SOUTH", "EAST", "WEST"})
    void testCarrackForAllDirections(String dir) {
        Position pos = new Position(2, 2);
        Carrack c = new Carrack(Compass.valueOf(dir), pos);

        assertEquals(3, c.getSize());
        assertEquals(3, c.getPositions().size());

        IPosition first = c.getPositions().get(0);
        IPosition second = c.getPositions().get(1);
        IPosition third = c.getPositions().get(2);

        // O Carrack cresce sempre para baixo (N/S) ou direita (E/W)
        if (dir.equals("NORTH") || dir.equals("SOUTH")) {
            assertAll(
                    () -> assertEquals(first.getRow() + 1, second.getRow(), "1º e 2º devem ser verticais consecutivos"),
                    () -> assertEquals(second.getRow() + 1, third.getRow(), "2º e 3º devem ser verticais consecutivos"),
                    () -> assertEquals(first.getColumn(), second.getColumn(), "Coluna igual"),
                    () -> assertEquals(second.getColumn(), third.getColumn(), "Coluna igual")
            );
        } else if (dir.equals("EAST") || dir.equals("WEST")) {
            assertAll(
                    () -> assertEquals(first.getColumn() + 1, second.getColumn(), "1º e 2º devem ser horizontais consecutivos"),
                    () -> assertEquals(second.getColumn() + 1, third.getColumn(), "2º e 3º devem ser horizontais consecutivos"),
                    () -> assertEquals(first.getRow(), second.getRow(), "Linha igual"),
                    () -> assertEquals(second.getRow(), third.getRow(), "Linha igual")
            );
        }
    }

    @ParameterizedTest(name = "Carrack criada na posição ({0}, {1}) deve manter coordenadas iniciais")
    @CsvSource({
            "1,1",
            "2,3",
            "4,6"
    })
    void testCarrackInitialPosition(int row, int col) {
        Position pos = new Position(row, col);
        Carrack c = new Carrack(Compass.NORTH, pos);
        IPosition p = c.getPositions().get(0);

        assertAll(
                () -> assertEquals(row, p.getRow(), "Linha inicial incorreta"),
                () -> assertEquals(col, p.getColumn(), "Coluna inicial incorreta")
        );
    }
}
