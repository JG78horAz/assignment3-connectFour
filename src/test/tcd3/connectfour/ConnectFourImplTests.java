package tcd3.connectfour;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConnectFourImplTests {

    private ConnectFourImpl g;

    @Mock
    private ConnectFour mockGame;

    private ByteArrayOutputStream buffer;
    private PrintStream out;

    @BeforeEach
    void setUp() {
        g = new ConnectFourImpl(Player.red);
        buffer = new ByteArrayOutputStream();
        out = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        if (mockGame != null) reset(mockGame);
    }

    @Test
    void newGameIsEmptyAndRedStarts() {
        assertEquals(Player.red, g.getPlayerOnTurn());
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                assertEquals(Player.none, g.getPlayerAt(r, c));
            }
        }
        assertFalse(g.isGameOver());
        assertEquals(Player.none, g.getWinner());
    }

    @Test
    void dropFallsToBottomAndTurnAlternates() {
        g.drop(3);
        assertEquals(Player.red, g.getPlayerAt(5, 3));
        assertEquals(Player.yellow, g.getPlayerOnTurn());

        g.drop(3);
        assertEquals(Player.yellow, g.getPlayerAt(4, 3));
        assertEquals(Player.red, g.getPlayerOnTurn());
    }

    @Test
    void fullColumnIsIgnoredAndTurnDoesNotChange() {
        for (int i = 0; i < 6; i++) g.drop(0);
        Player before = g.getPlayerOnTurn();
        g.drop(0);
        assertEquals(before, g.getPlayerOnTurn());
    }

    @Test
    void verticalWinWorks() {
        g.drop(0); // R
        g.drop(1); // Y
        g.drop(0); // R
        g.drop(1); // Y
        g.drop(0); // R
        g.drop(1); // Y
        g.drop(0); // R wins
        assertTrue(g.isGameOver());
        assertEquals(Player.red, g.getWinner());
    }

    @Test
    void horizontalWinWorks() {
        g.drop(0); // R
        g.drop(0); // Y
        g.drop(1); // R
        g.drop(1); // Y
        g.drop(2); // R
        g.drop(2); // Y
        g.drop(3); // R wins
        assertTrue(g.isGameOver());
        assertEquals(Player.red, g.getWinner());
    }

    @Test
    @Disabled
    void diagonalDownRightWinWorks() {
        g.drop(0); // R
        g.drop(1); // Y
        g.drop(1); // R
        g.drop(2); // Y
        g.drop(2); // R
        g.drop(2); // Y
        g.drop(2); // R
        g.drop(3); // Y
        g.drop(3); // R
        g.drop(3); // Y
        g.drop(3); // R wins
        assertTrue(g.isGameOver());
        assertEquals(Player.red, g.getWinner());
    }

    @Test
    void diagonalUpRightWinWorks() {
        g.drop(3); // R at (5,3)
        g.drop(2); // Y at (5,2)
        g.drop(2); // R at (4,2)
        g.drop(1); // Y at (5,1)
        g.drop(0); // R at (5,0)
        g.drop(1); // Y at (4,1)
        g.drop(1); // R at (3,1)
        g.drop(0); // Y at (4,0)
        g.drop(0); // R at (3,0)
        g.drop(6); // Y irgendwo
        g.drop(0); // R at (2,0) wins
        assertTrue(g.isGameOver());
        assertEquals(Player.red, g.getWinner());
    }

    @Test
    void resetClearsBoardAndSetsPlayer() {
        g.drop(3);
        g.reset(Player.yellow);
        assertEquals(Player.yellow, g.getPlayerOnTurn());
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                assertEquals(Player.none, g.getPlayerAt(r, c));
            }
        }
        assertFalse(g.isGameOver());
        assertEquals(Player.none, g.getWinner());
    }

    @Test
    void cli_input4_callsDropOnColumn3() {
        when(mockGame.isGameOver()).thenReturn(false, true, true);
        when(mockGame.getWinner()).thenReturn(Player.none);
        when(mockGame.toString()).thenReturn("BOARD\n");

        Scanner scanner = new Scanner(new ByteArrayInputStream("4\n".getBytes(StandardCharsets.UTF_8)));
        Main.run(mockGame, scanner, out);

        verify(mockGame, times(1)).drop(3);
    }

    @Test
    void cli_inputR_callsResetWithRed() {
        when(mockGame.isGameOver()).thenReturn(false, true, true);
        when(mockGame.getWinner()).thenReturn(Player.none);
        when(mockGame.toString()).thenReturn("BOARD\n");

        Scanner scanner = new Scanner(new ByteArrayInputStream("r\n".getBytes(StandardCharsets.UTF_8)));
        Main.run(mockGame, scanner, out);

        verify(mockGame, times(1)).reset(Player.red);
        verify(mockGame, never()).drop(anyInt());
    }

    @Test
    void cli_unknownCommand_printsMessage_andThenQuit() {
        when(mockGame.isGameOver()).thenReturn(false, false, false, false);
        when(mockGame.toString()).thenReturn("BOARD\n");

        Scanner scanner = new Scanner(new ByteArrayInputStream("xyz\nq\n".getBytes(StandardCharsets.UTF_8)));
        Main.run(mockGame, scanner, out);

        String printed = buffer.toString(StandardCharsets.UTF_8);
        assertTrue(printed.contains("Unknown command"));
        assertTrue(printed.contains("Ok, bye."));
        verify(mockGame, never()).drop(anyInt());
    }

    @Test
    void cli_helpCommand_printsHelp_andDoesNotCallDrop() {
        when(mockGame.isGameOver()).thenReturn(false, false, false, false);
        when(mockGame.toString()).thenReturn("BOARD\n");

        Scanner scanner = new Scanner(new ByteArrayInputStream("h\nq\n".getBytes(StandardCharsets.UTF_8)));
        Main.run(mockGame, scanner, out);

        String printed = buffer.toString(StandardCharsets.UTF_8);
        assertTrue(printed.contains("Available commands:"));
        assertTrue(printed.contains("1 .. 7 --> drop disc in column"));
        verify(mockGame, never()).drop(anyInt());
    }
}
