import React, { PureComponent } from 'react';
import PropTypes from 'prop-types';
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer
} from 'recharts';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#808000', '#000080', '#FFFF00'];

class SimpleBarChart extends PureComponent {
  static propTypes = {
    data: PropTypes.arrayOf(PropTypes.shape({
      name: PropTypes.string,
    })).isRequired,
    max: PropTypes.number.isRequired,
    handleClick: PropTypes.func
  }
  
	render () {
  	return (
      <ResponsiveContainer height={600} width='100%'>
        <BarChart 
          width={600} 
          height={300} 
          data={this.props.data} 
          margin={{top: 20, left: -10}}>
          <CartesianGrid strokeDasharray="3 3"/>
          <XAxis dataKey="name"/>
          <YAxis yAxisId="left" orientation="left" stroke="#f3c363" domain={[0, this.props.max]}/>
          <Tooltip/>
          <Legend />    
          {this.props.data && Object.keys(this.props.data[0]).filter(e => e!=='name').map((e, index) => (
            <Bar key={e} yAxisId="left" dataKey={e} fill={COLORS[index % COLORS.length]} onClick={data=>this.props.handleClick(data)}/>
          ))}                    
        </BarChart>
      </ResponsiveContainer>
    );
  }
}

export default SimpleBarChart;
