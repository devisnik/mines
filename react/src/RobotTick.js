import { connect } from 'react-redux';
import { robotTick } from './actions';
import Tick from './Tick';

const RobotTick = connect((state) => ({intervalMs: state.robotInterval}), {onTick: robotTick})(Tick);

export default RobotTick; 
