import { connect } from 'react-redux';
import { clockTick } from './actions';
import Tick from './Tick';

const ClockTick = connect((state) => ({intervalMs: state.clockInterval}), {onTick: clockTick})(Tick);

export default ClockTick;
