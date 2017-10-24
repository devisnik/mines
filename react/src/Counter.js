import React from 'react';
import Digit from './Digit';

const Counter = ({ value=0 }) => {
    return (
        <div>
            <Digit value={ Math.floor(value/100) % 10 } />
            <Digit value={ Math.floor(value/10) % 10 } />
            <Digit value={ value % 10 } />
        </div>
    );
}

export default Counter;
