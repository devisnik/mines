import React from 'react';
import Cell from './Cell';

const Row = ({imageIds, onCellClicked, index}) => {
    const cellSize = 100/imageIds.length + '%';
    const cells = imageIds.map((i, pos) => {
        return (
            <Cell key={pos} id={pos} imageId={i} cellSize={cellSize}
                onCellClicked={(e, i) => onCellClicked(e, i, index)} />
        );
    });
    return (
        <div className="board-row" style={{marginBottom: -4}}>{cells}</div>
    );
}

Row.propTypes = {
    imageIds:       React.PropTypes.arrayOf(React.PropTypes.number).isRequired,
    index:          React.PropTypes.number.isRequired,
    onCellClicked:  React.PropTypes.func.isRequired
}

export default Row;
