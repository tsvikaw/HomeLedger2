import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";

import { selectSheet, selectLabelsLine, selectDateColumn, selectAmountColumn, selectDescriptionColumn, doneMappingExcel } from "./importStages"
import s from './Import.module.scss';

class ExcelMapper extends Component {

  constructor(props) {
    super(props);
  
  this.state = {
    stage:selectSheet,
    highlightedLine:-1,
    highlightedColumn: -1,
    labelLine: -1,
    selectSheet:-1,
    sheetNumber:0,
    dateColumn:-1,
    amountColumn: -1,
    descriptionColumn:-1,
  };
  } 

  static propTypes = {
    excelContent: PropTypes.array.isRequired,
    parseExcelLines: PropTypes.func.isRequired
  };

  static defaultProps = {
    excelContent: [],
  };

  componentDidMount() {
    if(this.props.excelContent.length === 1){
      this.setState({stage : selectLabelsLine});
    }
  }

  mouseEnterContentLine(index, index2){
    switch(this.state.stage){
      case selectLabelsLine:
        this.setState({highlightedLine : index});
        break;
      case selectDateColumn:
      case selectAmountColumn:
      case selectDescriptionColumn:
        this.setState({highlightedColumn : index2});
        break;
      default:
    }
  }

  getHighlightedClass(index, index2){
    switch(this.state.stage){
      case selectLabelsLine:
          return index === this.state.highlightedLine ? s.labelsline : "";
      case selectDateColumn:
      case selectAmountColumn:
      case selectDescriptionColumn:
          return index2 === this.state.highlightedColumn ? s.highlight : "";
      default:
    }
  }

  handleContentSelect(index, index2){
    switch(this.state.stage){
      case selectLabelsLine:
        this.setState({labelLine: index, stage:selectDateColumn});
        break;
      case selectDateColumn:
          this.setState({dateColumn: index2, stage:selectAmountColumn});
          break;
      case selectAmountColumn:
          this.setState({amountColumn: index2, stage:selectDescriptionColumn});
          break;
      case selectDescriptionColumn:
          this.setState({descriptionColumn: index2, stage:doneMappingExcel});
          this.props.parseExcelLines(this.state.sheetNumber, this.state.labelLine, this.state.dateColumn, this.state.amountColumn, index2)
          break;
      default:
    }
    if(this.state.stage === selectLabelsLine){
      this.setState({labelLine: index, stage:selectDateColumn});
    } else if(this.state.stage === selectDateColumn){
      this.setState({dateColumn: index2, stage:selectAmountColumn});
    }
  }

  handleSheetPrevious = event=>{
    this.setState({sheetNumber: this.state.sheetNumber -1});
  }

  handleSheetNext = event=>{
    this.setState({sheetNumber: this.state.sheetNumber +1});
  }

  handleSheetSelect= event=>{
    this.setState({stage: selectLabelsLine});
  }

  render() {
  return (<div>
                        <h1>Prepare excel for import</h1>
                        {this.state.stage === selectSheet && (<h3>Is this the right <span className={s.colored}>sheet? &nbsp;</span> 
                          <button disabled={this.state.sheetNumber === 0 } onClick={this.handleSheetPrevious}>&lt;</button>
                          <button onClick={this.handleSheetSelect}>Yes</button>
                          <button disabled={this.state.sheetNumber === this.props.excelContent.length -1} onClick={this.handleSheetNext}>&gt;</button>
                        </h3>)}
                        {this.state.stage === selectLabelsLine && (<h3>Please select the <span className={s.colored}>headers</span> line</h3>)}
                        {this.state.stage === selectDateColumn && (<h3>Now let's select the transaction <span className={s.colored}>date</span> column</h3>)}
                        {this.state.stage === selectAmountColumn && (<h3>And transaction  <span className={s.colored}>debit amount</span> column</h3>)}
                        {this.state.stage === selectDescriptionColumn && (<h3>Now the pick the  <span className={s.colored}>description</span> column</h3>)}
                        <hr/>
                        <table className={s.excel}>
                          <tbody>
                            <tr>
                              <td></td>
                              <th>A</th>
                              <th>B</th>
                              <th>C</th>
                              <th>D</th>
                              <th>E</th>
                              <th>F</th>
                              <th>G</th>
                              <th>H</th>
                              <th>I</th>
                              <th>J</th>
                              <th>K</th>
                              <th>L</th>
                              <th>M</th>
                              <th>N</th>
                            </tr>
                          {
                            this.props.excelContent[this.state.sheetNumber]
                            .filter((a,i) => i<30)
                            .map((line, index) => index >= this.state.labelLine && (
                              <tr key={index} className={this.getHighlightedClass(index)}>
                                <td className={s.rowheader}>{index+1}</td>
                                {line.map((cell, index2) => 
                                index2 !== this.state.dateColumn && 
                                index2 !== this.state.amountColumn && 
                                index2 !== this.state.descriptionColumn && 
                                (
                                  <td key={index2} className={this.getHighlightedClass(index, index2)}>
                                  <div 
                                    onMouseEnter={() => this.mouseEnterContentLine(index, index2)} 
                                    onClick={()=> this.handleContentSelect(index,index2)}
                                    >{cell ? cell : (<span>&nbsp;</span>)}</div>
                                  </td>
                                ))}  
                              </tr>
                            ))
                          }
                          </tbody>
                        </table>
                      </div>
  );
  }
}

function mapStateToProps(state) {
  return {
    excelContent: state.excelLines.excelContent,
  };
}

export default connect(mapStateToProps)(ExcelMapper);