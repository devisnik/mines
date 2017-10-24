import React from 'react';
import Row from './Row';
import { openCell, flagCell } from './actions';
import { connect } from 'react-redux';

const Board = ({ data, onCellClicked }) => {
    const rows = data.map((row,index) => {
        return (
            <Row key={index} index={index} onCellClicked={onCellClicked} imageIds={row}/>
        );
    });
    return (
        <div>{rows}</div>
    );
}
    
const mapStateToProps = (state) => {
    return {
        data: state.board,
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        onCellClicked: (e, x, y) => { 
            if(e.shiftKey) {
                dispatch(openCell(x,y));
            }
            else {
                dispatch(flagCell(x,y));
            }
        }
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Board)
