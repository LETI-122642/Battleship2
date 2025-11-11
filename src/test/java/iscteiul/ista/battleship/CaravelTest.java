package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes parametrizados da classe Caravel (tamanho 2)")
class CaravelTest {

    @ParameterizedTest(name = "Caravel com direção {0} deve gerar duas posições consecutivas corretas")
    @ValueSource(strings = {"NORTH", "SOUTH", "EAST", "WEST"})
    void testCaravelForAllDirections(String dir) {
        Position pos = new Position(3, 4);
        Caravel c = new Caravel(Compass.valueOf(dir), pos);

        assertEquals(2, c.getSize());
        assertEquals(2, c.getPositions().size());

        IPosition first = c.getPositions().get(0);
        IPosition second = c.getPositions().get(1);

        // A Caravel cresce sempre para baixo (NORTH/SOUTH) ou para a direita (EAST/WEST)
        if (dir.equals("NORTH") || dir.equals("SOUTH")) {
            assertEquals(first.getRow() + 1, second.getRow(), "Deve aumentar linha");
            assertEquals(first.getColumn(), second.getColumn(), "Coluna deve manter-se igual");
        } else if (dir.equals("EAST") || dir.equals("WEST")) {
            assertEquals(first.getColumn() + 1, second.getColumn(), "Deve aumentar coluna");
            assertEquals(first.getRow(), second.getRow(), "Linha deve manter-se igual");
        }
    }

    @ParameterizedTest(name = "Caravel criada na posição ({0}, {1}) deve manter coordenadas iniciais")
    @CsvSource({
            "1,1",
            "2,2",
            "3,4"
    })
    void testCaravelInitialPosition(int row, int col) {
        Position pos = new Position(row, col);
        Caravel c = new Caravel(Compass.NORTH, pos);
        IPosition p = c.getPositions().get(0);

        assertAll(
                () -> assertEquals(row, p.getRow(), "Linha incorreta"),
                () -> assertEquals(col, p.getColumn(), "Coluna incorreta")
        );
    }
}
