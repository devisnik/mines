import { ROBOT_TICK, CLOCK_TICK, BOARD_OPEN_CELL, BOARD_FLAG_CELL } from './actions';
import Mines from 'mines-module';

var game = new Mines(32,18,90);

const initialState = {
    board: game.board(),
    bombCounter: game.bombCount(),
    timeCounter: game.time(),
    isRunning: game.isRunning(),
    clockInterval: 1000,
    robotInterval: 400
}

export default function minesApp(state = initialState, action) {
    console.log(action);
    function status(cell, game) {
        if (game.isRunning()) {
            return '';
        }
        else if (game.isExploded()) {
            return 'Sorry, you lose!';
        }
        return 'Congratulations, you win!';
    }
    switch (action.type) {
        case ROBOT_TICK: {
            game.robotMove();
            return { ...state,
                status: status(action.cell, game),
                board: game.board(),
                bombCounter: game.bombCount(),
                isRunning: game.isRunning()
            }
        }
        case CLOCK_TICK: {
            game.clockTick();
            return { ...state,
                timeCounter: game.time(),
            };
        }
        case BOARD_FLAG_CELL: {
            const {x, y} = action.cell;
            game.flag(x,y);
            return { ...state,
                status: status(action.cell, game),
                board: game.board(),
                bombCounter: game.bombCount(),
                isRunning: game.isRunning()
            };
        }
        case BOARD_OPEN_CELL: {
            const {x,y} = action.cell;
            game.open(x,y);
            return { ...state,
                status: status(action.cell, game),
                board: game.board(),
                isRunning: game.isRunning()
            };
        }
        default: {
            return state;
        }
    }
}

