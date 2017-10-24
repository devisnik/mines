import React from 'react';

import './App.css';

import Board from './Board';
import Counter from './Counter';
import ClockTick from './ClockTick';
import RobotTick from './RobotTick';

const App = ({status, bombCounter, timeCounter, isRunning}) => {
    return (
      <div className="App">
        <div className="App-header">
        </div>
        { isRunning && <ClockTick /> }
        { isRunning && <RobotTick /> }
        <div className="board-container">
            <div className="counters">
                <Counter value={timeCounter}/>
                <Counter value={bombCounter}/>
            </div>
            <Board />
        </div>
        <p>{status}</p>
      </div>
    );
}   

export default App;
