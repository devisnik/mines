import React from 'react';
import './Digit.css';

const images = [...Array(10).keys()]
    .map( i => { return require('./images/counter_'+ i + '.gif') });

const Digit = ({ value }) => {
    return (
       <img className="Digit" src={images[value]} alt='Number {value}'/>
    );
}

export default Digit;
