import React, { Component } from "react";
import { PropTypes } from "prop-types";
import { connect } from "react-redux";
import ExcelForm from "./ExcelForm";
import SelectInput from "../common/SelectInput";
import { toast } from 'react-toastify';

import {
  Row,
  Col,
  Breadcrumb,
  BreadcrumbItem,
  FormGroup,
  ButtonGroup,
  Input,
} from "reactstrap";

import Widget from "../../components/Widget/Widget";
import s from './Import.module.scss';

import { loadExcelLines, readExcel, submitExcelLines, readExcelReset, loadExcelLinesReset} from '../../actions/excelActions';
import { fetchAccounts } from "../../actions/accounts";
import { fetchCategories } from "../../actions/categories";
import { selectSheet, selectAccount, manualSort } from "./importStages"
import ExcelMapper from "./ExcelMapper";

class ImportPage extends Component {

  constructor(props) {
    super(props);

    this.state = {
      errors: {},
      account: "",
      filePath: "",
      stage: selectSheet,
      hideIrrelevant: false
    };

    this.parseExcelLines = this.parseExcelLines.bind(this);
  }

  static propTypes = {
    dispatch: PropTypes.func.isRequired,
    excelLines: PropTypes.array,
    savedExcelLines: PropTypes.array,
    excelContent: PropTypes.array,
    categories: PropTypes.array, // eslint-disable-line
    accounts: PropTypes.array, // eslint-disable-line
    message: PropTypes.string, // eslint-disable-line
  };

  static defaultProps = {
    categories: [],
    accounts: [],
    excelLines: [],
    savedExcelLines: [],
    excelContent: [],
    message: ""
  };

  componentDidMount() {
    this.props.dispatch(fetchCategories());
    this.props.dispatch(fetchAccounts());
  }

  getSnapshotBeforeUpdate(prevProps) {
    return {failureMessage: !prevProps.message && this.props.message,
      linesLoaded: (!prevProps.excelLines ||  prevProps.excelLines.length === 0)
      && this.props.excelLines && this.props.excelLines.length > 0,
      linesSaved: (!prevProps.savedExcelLines ||  prevProps.savedExcelLines.length === 0)
      && this.props.savedExcelLines && this.props.savedExcelLines.length > 0
    }
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    if(snapshot.failureMessage){
      this.showLoadFailureMessage();
      this.setState({filePath: ""});
      this.props.dispatch(readExcelReset());
    }
    if(snapshot.linesLoaded){
      this.setState({stage: selectAccount});
    }
    if(snapshot.linesSaved){
      this.showSaveSuccessMessage();
    }
  }

  showLoadFailureMessage() {
    toast.error(this.props.message, {
      position: "bottom-right",
      autoClose: 5000,
      closeOnClick: true,
      pauseOnHover: false,
      draggable: true
    });
  }

  showSaveSuccessMessage() {
    toast.success(this.props.savedExcelLines.length + " lines were saved", {
      position: "bottom-right",
      autoClose: 5000,
      closeOnClick: true,
      pauseOnHover: false,
      draggable: true
    });
  }

  handleFilePick = event => {
    this.setState({ filePath: event.target.value });
  };

  handleFileAutoParse = event => {
    this.props.dispatch(loadExcelLines(this.state.filePath));
  };

  handleFileRead = event => {
    this.props.dispatch(readExcel(this.state.filePath));
  };

  resetFile = () => {
    this.props.dispatch(readExcelReset());
    this.props.dispatch(loadExcelLinesReset());
    
    this.setState({filePath: "", stage: selectSheet});
  };

  handleToggleIrrelevantLines = event => {
    this.setState({
      hideIrrelevant: !this.state.hideIrrelevant
    });
    this.forceUpdate();
  }

  handleChange = (line, event) => {
    line.line.categoryId = event.target.value;
    this.props.excelLines
      .filter(l => l.line.description === line.line.description)
      .forEach(
        l =>
          (l.line.categoryId = l.line.categoryId
            ? l.line.categoryId
            : event.target.value)
      );
    this.forceUpdate();
  };

  handleDelete = (line, event) => {
    line.isDeleted = true;
    this.forceUpdate();
  };

  handleSelectRecommended = (line, event) => {
    line.line.categoryId = line.categoryHits[0].category.id;
    line.line.categoryName = line.categoryHits[0].category.name;
    this.forceUpdate();
  };

  handleSave = event => {
    event.preventDefault();
    if (this.state.account === "") {
      alert("Please select an account");
      return;
    }
    this.props.excelLines.forEach(l => (l.line.account = this.state.account));
    this.props.dispatch(submitExcelLines(
      this.props.excelLines
        .filter(l => !l.isDeleted && !l.previouselyHit)
        .map(l => l.line)
    ));
  };

  parseExcelLines(sheetNumber, labelLine, dateColumn, amountColumn, descriptionColumn) {
    this.props.dispatch(loadExcelLines(
      this.state.filePath, sheetNumber,labelLine + 1,dateColumn, amountColumn,descriptionColumn));
    this.setState({stage:selectAccount});
  }

  render() {
    return (
      <div>
        <Breadcrumb>
          <BreadcrumbItem active>Import</BreadcrumbItem>
        </Breadcrumb>
        <h1 className="page-title mb-lg">
          <span className="fw-semi-bold">Expences</span> - Import
        </h1>
        <Row>
          <Col lg={12}>
            <Widget>
              {(
                  <div>
                    {this.props.excelLines.length || this.props.excelContent.length ? (
                      <div>
                        <ButtonGroup>
                          <h4>{this.state.filePath}</h4>
                          <button className="btn btn-link"  
                            onClick={this.resetFile}>Unload</button>
                        </ButtonGroup>
                        <hr />
                      </div>
                    ) : (
                      <div>
                        <ButtonGroup>
                        <Input
                            type="text"
                            placeholder="File ..."
                            className= {"form-control " + s.wide} 
                            value={this.state.filePath}
                            onChange={this.handleFilePick}
                          />
                          &nbsp;
                        <button className="btn btn-primary" disabled={this.state.filePath === ""} 
                          onClick={()=> this.handleFileRead()}>Read</button>
                        {/* <button onClick={()=> this.handleFileAutoParse()}>Parse</button> */}
                        </ButtonGroup>
                      </div>
                    )}
                    <div>
                    {this.props.excelContent && this.props.excelContent.length > 0 && !this.props.excelLines.length && (
                        <ExcelMapper parseExcelLines={this.parseExcelLines} />
                    )}
                    </div>
                    <div>
                    {this.state.stage === selectAccount && (
                          <div>
                            <h1>Please select the  <span className={s.colored}>account</span> for this data</h1>
                            <SelectInput
                              name="account"
                              label=""
                              value={this.state.account}
                              defaultOption="Select Account"
                              options={this.props.accounts.map(account => ({
                                value: account,
                                text: account
                              }))}
                              onChange={e =>
                                this.setState({ account: e.target.value, stage: manualSort })
                              }
                            />
                          </div>
                    )}
                    {this.state.stage === manualSort && (
                        <div>
                          <h1>Manualy sort the category for each expence</h1>
                          {this.state.account && (
                            <div>
                              <button className="btn btn-link"
                                onClick={this.handleToggleIrrelevantLines}>
                                Show/hide irrelevant lines
                              </button>
                              <ExcelForm
                                excelLines={this.props.excelLines}
                                categories={this.props.categories}
                                errors={this.state.errors}
                                handleToggleIrrelevantLines={this.state.handleToggleIrrelevantLines}
                                onChange={this.handleChange}
                                onDelete={this.handleDelete}
                                onSelectRecommended={this.handleSelectRecommended}
                                onSave={this.handleSave}
                              />
                            </div>
                          )}
                        </div>
                    )}
                    </div>
                  </div>
                )}
            </Widget>
          </Col>
        </Row>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    savedExcelLines: state.excelLines.savedExcelLines,
    excelLines: state.excelLines.excelLines,
    excelContent: state.excelLines.excelContent,
    message: state.excelLines.message,
    categories: state.categories.categories,
    accounts: state.accounts.accounts,
  };
}

export default connect(mapStateToProps)(ImportPage);