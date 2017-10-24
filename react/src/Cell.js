import React from 'react';

function as2Digits(i) {
    return (i < 10 ? '0' : '') + i;
}

const images = [...Array(16).keys()]
    .map( i => { return require('./images/dark_image_'+ as2Digits(i) + '.png') });

const Cell = ({id, onCellClicked, imageId, cellSize}) => {
    let cellStyle = {
        width: cellSize,
        height: cellSize,
    }
    return (
        <img className="Cell" alt="Cell" style={cellStyle}
        onClick={(e) => {onCellClicked(e, id)} }
        src={images[imageId]}/>
    );
}

Cell.propTypes = {
    id:             React.PropTypes.number.isRequired,
    imageId:        React.PropTypes.number.isRequired,
    onCellClicked:  React.PropTypes.func.isRequired
}
export default Cell;
