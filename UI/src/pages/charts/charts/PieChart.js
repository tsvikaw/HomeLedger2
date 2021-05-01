import React, { PureComponent } from 'react';
import { PieChart, Pie, Cell, Legend, ResponsiveContainer } from 'recharts';
import { numberWithCommas } from '../../../utils/stringUtils';
import { PropTypes } from 'prop-types';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#808000', '#000080', '#FFAACC', '#803333', '#AA0080', '#BBFF00'];
const RADIAN = Math.PI / 180;                   

class SimplePieChart extends PureComponent {
  static propTypes = {
    data: PropTypes.array.isRequired,
    handleClick: PropTypes.func
  }
  renderCustomizedLabel = ({ cx, cy, midAngle, innerRadius, outerRadius, percent, index }) => {
    const radius = innerRadius + (outerRadius - innerRadius) * 0.5;
    const x  = cx + radius * Math.cos(-midAngle * RADIAN);
    const y = cy  + radius * Math.sin(-midAngle * RADIAN);
  
   return (
     <text x={x} y={y} fill="white" textAnchor={x > cx ? 'start' : 'end'} 	dominantBaseline="central">
       {this.props.data[index].name +  ` ${(percent * 100).toFixed(0)}%`}
     </text>
   );
  }

	render () {
  	return (
      <ResponsiveContainer height={400} width='100%'>
        <PieChart onMouseEnter={this.onPieEnter}>
          <Pie
            data={this.props.data} 
            cx='50%' 
            cy={180} 
            labelLine={false}
            label={this.renderCustomizedLabel}
            outerRadius={180} 
            fill="#8884d8"
            dataKey="value"
            onClick={data=>this.props.handleClick(data)}
          >
            {
              this.props.data.map((entry, index) => <Cell key={entry} fill={COLORS[index % COLORS.length]}/>)
            }
          </Pie>
          <Legend
            layout="vetical"
            verticalAlign="middle" 
            align="right"
            payload={
              this.props.data.map(
                (entry, index) => ({
                  id: entry.id,
                  type: "square",
                  color: `${COLORS[index % COLORS.length]}`,
                  value: `${entry.name} (${numberWithCommas(entry.value)})`,
                })
              )
            }
          />
        </PieChart>
      </ResponsiveContainer>
    );
  }
}

export default SimplePieChart;
