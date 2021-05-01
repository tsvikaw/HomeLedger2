import React, { PureComponent } from 'react';
import { Row, Col, Breadcrumb, BreadcrumbItem, FormGroup, Input } from 'reactstrap';

import Widget from '../../../components/Widget/Widget';
import { fetchSummaryResults } from "../../../actions/summaryResults";
import { fetchCategories } from '../../../actions/categories';
import { pad } from "../../../utils/stringUtils";

import BarChart from '../charts/BarChart';
import PropTypes from "prop-types";
import { connect } from "react-redux";
import DropdownTreeSelect from "react-dropdown-tree-select";

import { YearSelector, MonthSelector } from "../../query/Filters";

const data = [
  {name: 'Page A', uv: 30000, pv: 2400, amt: 2400},];
const barMonths = 6;

class SummaryChart extends PureComponent {

  constructor(props) {
    super(props);

    this.state = {
      description: "",
      startYear: new Date().getFullYear(),
      startMonth: 1,
      endYear: new Date().getFullYear(),
      endMonth:
        new Date().getMonth() === 11 ? 11 : pad(new Date().getMonth() + 1, 2),
      startAmount: 0,
      endAmount: 1000000,
      account: null,
      categoryIds: [],
      pageSize: 1000000,
      pageNumber: 1,
      sortBy: "date",
      sortAsc: false,
      data: null,
      isFiltersDirty: false,
    };

    this.max = 0; // this needs to be out of state to fix a bug in the chart 3rd party component
  }

  
  static propTypes = {
    dispatch: PropTypes.func.isRequired,
    summaryResults: PropTypes.array, // eslint-disable-line
    categories: PropTypes.array, // eslint-disable-line
    accounts: PropTypes.array, // eslint-disable-line
    isFetching: PropTypes.bool
  };

  static defaultProps = {
    isFetching: false,
    summaryResults: null,
    categories: [],
    accounts: []
  };

  componentDidMount() {
    if (process.env.NODE_ENV === "development") {
      this.props.dispatch(fetchCategories());
    }
  }

  getSnapshotBeforeUpdate(prevProps) {
    return {categoriesLoaded:
       (!prevProps.categories || !prevProps.categories.length) 
        && this.props.categories && this.props.categories.length ,
        }
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    if(snapshot.categoriesLoaded){
      this.handleCategoriesLoaded();
    }
  }

  handleCategoriesLoaded() {
    if(this.props.location.state){
      this.setStateFromRoute(this.props.location.state.previousState);
    } else {
      let day2day = this.props.categories.filter(c => c.name === "day to day")[0];
      this.selectCategories([day2day], () => this.props.dispatch(fetchSummaryResults(this.state)));
    }
  }

  setStateFromRoute(stateFromRoute){
    this.setState(stateFromRoute, ()=> this.props.dispatch(fetchSummaryResults(this.state)));
  }

  getCategoryTreeRootNode(){
    if(!this.props.categories || !this.props.categories.length){
      return null;
    }
    let root = this.props.categories.filter(c => c.name === "root")[0];
    return this.createNode(root);
  }

  replaceCategoryIdWithName(summaryResults) {
    if(!summaryResults){
      return this.data;
    }
    let max = 0;
    let data = JSON.parse(JSON.stringify(summaryResults));
    data.forEach(dateGroupSummary => {
      Object.keys(dateGroupSummary)
        .filter(e => e !== 'name')
        .forEach(categoryIdAttribute => {
          // create a new attribute for name and delete the old one
          const value = Object.getOwnPropertyDescriptor(dateGroupSummary, categoryIdAttribute);
          Object.defineProperty(dateGroupSummary, this.getCategoryName(categoryIdAttribute), value);
          max = Math.max(max, value.value)
          delete dateGroupSummary[categoryIdAttribute];
        });
    });
    this.max = max;
    return data;
  }

  getCategoryName(categoryIdAttribute) {
    return this.props.categories.filter(c => c.id === categoryIdAttribute)[0].name;
  }

  createNode(category) {
    let node = {
      label: category.name,
      value: category.id,
      checked:
        this.state.categoryIds.filter(item => item.id === category.id)
          .length > 0,
      children: []
    };

    category.children.forEach(childCategory => {
      node.children.push(this.createNode(childCategory));
    });

    return node;
  }

  clickedFilter() {
    this.setState({data: null, isFiltersDirty: true}, ()=> this.props.dispatch(fetchSummaryResults(this.state)));
  }

  selectCategories(categories, callback) {
    let selectedCategories = [];
    categories.forEach(c => selectedCategories.push({ id: c.id }));
    this.setState({categoryIds: selectedCategories}, callback);
  }

  handleChangeDescription = e => {
    this.setState({ description: e.target.value });
  };

  handleStartYearChange = e => {
    this.setState({ startYear: e.target.value });
  };

  handleStartMonthChange = e => {
    this.setState({ startMonth: e.target.value });
  };

  handleEndYearChange = e => {
    this.setState({ endYear: e.target.value });
  };

  handleEndMonthChange = e => {
    this.setState({ endMonth: e.target.value });
  };

  handleCategoriesTreeChange = (currentNode, selectedNodes) => {
    if (selectedNodes) {
      this.selectCategories(
        selectedNodes.map(sn => 
          this.props.categories.filter(c => c.id === sn.value)[0])
      );
    }
  };

  handleAccountChange = e => {
    this.setState({
      account: e.target.value === "default" ? null : e.target.value
    });
  };

  handleRouteToQuery = () =>{
    this.props.history.push({pathname:'/app/query', 
    state:{previousState: this.state, routedFrom: this.props.location.pathname, routeTargetState: this.state}});
  }

  handleBarClick = (data) =>{
    let currentState = Object.assign({}, this.state);
    this.props.categories.filter(c => 
      Object.keys(data)
        .filter(attribute => attribute === c.name).length > 0
    ).forEach(c => {
      currentState.categoryIds = [{id:c.id}];
    });
    currentState.startYear = parseInt(data.name.split('-')[0]);
    currentState.endYear = currentState.startYear;
    const monthPeriodIndex = parseInt(data.name.split('-')[1]);
    currentState.startMonth = pad((monthPeriodIndex - 1) * barMonths + 1, 2);
    currentState.endMonth = pad(parseInt(monthPeriodIndex) * barMonths, 2);
    this.props.history.push({pathname:'/app/query', 
    state:{previousState: this.state, routedFrom: this.props.location.pathname, routeTargetState: currentState}});
  }

  render() {
    return(
      <div>
        <Breadcrumb>
          <BreadcrumbItem active>Charts</BreadcrumbItem>
        </Breadcrumb>
        <h1 className="page-title mb-lg">Category Totals</h1>
            <Widget
              title={
                <div>
                  <div className="mt-n-xs">
                    <FormGroup className="form-group">
                      <Row>
                        <Col lg={1}>
                          <MonthSelector
                            id="startMonthSelect"
                            month={this.state.startMonth}
                            handleChange={this.handleStartMonthChange}
                          ></MonthSelector>
                          <br></br>
                          <MonthSelector
                            id="endMonthSelect"
                            month={this.state.endMonth}
                            handleChange={this.handleEndMonthChange}
                          ></MonthSelector>
                        </Col>
                        <Col lg={1}>
                          <YearSelector
                            id="startYeaSelector"
                            year={this.state.startYear}
                            handleChange={this.handleStartYearChange}
                          ></YearSelector>
                          <br></br>
                          <YearSelector
                            id="endYeaSelector"
                            year={this.state.endYear}
                            handleChange={this.handleEndYearChange}
                          ></YearSelector>
                        </Col>
                        <Col lg={2}>
                          <Input
                            type="search"
                            placeholder="Description..."
                            className="form-control input-sm"
                            value={this.state.description}
                            onChange={this.handleChangeDescription}
                          />
                          <br></br>
                          <Input
                            type="select"
                            name="select"
                            className="form-control input-sm"
                            id="accountSelect"
                            value={this.state.account || ""}
                            onChange={this.handleAccountChange}
                          >
                            <option key="0" value="default">
                              Select account
                            </option>
                            {this.props.accounts &&
                              this.props.accounts.map(account => (
                                <option key={account} value={account}>
                                  {account}
                                </option>
                              ))}
                          </Input>
                        </Col>
                        <Col lg={2}>
                          {this.getCategoryTreeRootNode() && (
                            <DropdownTreeSelect
                              data={this.getCategoryTreeRootNode()}
                              onChange={this.handleCategoriesTreeChange}
                            /> )}
                        </Col>
                        <Col lg={2}>
                          <button
                            className="btn-rounded width-100 mb-xs mr-xs btn btn-outline btn-success"
                            onClick={() => this.clickedFilter()}
                          >
                            <span className="circle bg-white mr-xs">
                              <i className="glyphicon glyphicon-equalizer btn-success"></i>
                            </span>
                            {this.state.isFiltersDirty?"Chart":"Filter"}
                          </button>
                          {this.state.isFiltersDirty && (
                            <button
                            className="btn-rounded width-100 mb-xs mr-xs btn btn-outline btn-success"
                            onClick={this.handleRouteToQuery}
                          >
                            <span className="circle bg-white mr-xs">
                              <i className="glyphicon glyphicon-th-list btn-success"></i>
                            </span>
                            Details
                          </button>
                          )}
                        </Col>
                      </Row>
                    </FormGroup>
                  </div>
                </div>
              }>
                {this.props.categories &&
                 this.props.categories.length && 
                 this.props.summaryResults && (
              <BarChart data={
                 this.replaceCategoryIdWithName(this.props.summaryResults)} max={this.max}
                 handleClick={(data)=> this.handleBarClick(data)} />
              )}
            </Widget>
      </div>
    )
  }
}

function mapStateToProps(state) {
  return {
    isFetching: state.summaryResults.isFetching,
    summaryResults: state.summaryResults.summaryResults,
    categories: state.categories.categories,
    accounts: state.accounts.accounts
  };
}

export default connect(mapStateToProps)(SummaryChart);
