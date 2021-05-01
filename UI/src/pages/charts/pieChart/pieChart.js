import React, { PureComponent } from "react";
import {
  Row,
  Col,
  Breadcrumb,
  BreadcrumbItem,
  FormGroup,
  Input,
  CardDeck,
  Card
} from "reactstrap";

import Widget from "../../../components/Widget/Widget";
import { fetchPieResults } from "../../../actions/pieResults";
import { fetchCategories } from "../../../actions/categories";
import { pad, numberWithCommas } from "../../../utils/stringUtils";

import PropTypes from "prop-types";
import { connect } from "react-redux";

import { YearSelector, MonthSelector } from "../../query/Filters";
import SimplePieChart from "../charts/PieChart";

const data = [];

class CategoryPieChart extends PureComponent {
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
      isFiltersDirty: false
    };
  }

  static propTypes = {
    dispatch: PropTypes.func.isRequired,
    pieResults: PropTypes.array, // eslint-disable-line
    categories: PropTypes.array, // eslint-disable-line
    accounts: PropTypes.array, // eslint-disable-line
    isFetching: PropTypes.bool
  };

  static defaultProps = {
    isFetching: false,
    pieResults: null,
    categories: [],
    accounts: []
  };

  componentDidMount() {
    if (process.env.NODE_ENV === "development") {
      this.props.dispatch(fetchCategories());
    }
  }

  getSnapshotBeforeUpdate(prevProps) {
    return {
      categoriesLoaded:
        (!prevProps.categories || !prevProps.categories.length) &&
        this.props.categories &&
        this.props.categories.length
    };
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    if (snapshot.categoriesLoaded) {
      this.handleCategoriesLoaded();
    }
  }

  handleCategoriesLoaded() {
    if (this.props.location.state) {
      this.setStateFromRoute(this.props.location.state.previousState);
    } else {
      let day2day = this.props.categories.filter(
        c => c.name === "day to day"
      )[0];
      this.selectCategory(day2day, () =>
        this.props.dispatch(fetchPieResults(this.state))
      );
    }
  }

  setStateFromRoute(stateFromRoute) {
    this.setState(stateFromRoute, () =>
      this.props.dispatch(fetchPieResults(this.state))
    );
  }

  getCurrentCategoryAncesstors() {
    let ancesstors = [];
    let current = this.getCategory(this.state.categoryIds[0].id);

    while (current.parentId) {
      ancesstors.push({ id: current.id, name: current.name });
      current = this.getCategory(current.parentId);
    }
    ancesstors.push({ id: current.id, name: current.name });
    ancesstors.reverse();
    ancesstors.pop();
    return ancesstors;
  }

  getCategory(categoryIdAttribute) {
    return this.props.categories.filter(c => c.id === categoryIdAttribute)[0];
  }

  clickedFilter() {
    this.setState({ data: null, isFiltersDirty: true }, () =>
      this.props.dispatch(fetchPieResults(this.state))
    );
  }

  selectCategory(category, callback) {
    let selectedCategories = [];
    selectedCategories.push({ id: category.id });
    this.setState({ categoryIds: selectedCategories }, callback);
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

  handleAccountChange = e => {
    this.setState({
      account: e.target.value === "default" ? null : e.target.value
    });
  };

  handleTreeClick(id) {
    this.selectCategory(this.getCategory(id), () =>
      this.props.dispatch(fetchPieResults(this.state))
    );
  }

  handleSliceClick(data) {
    let cat = this.getCategory(data.id);
    let previousState = Object.assign({}, this.state); 
    let callback = cat.children.length ? 
      () => { this.props.dispatch(fetchPieResults(this.state)) } :
      () => { 
        this.handleRouteToQuery(previousState); 
      };

    this.selectCategory(cat, callback);
  }

  handleRouteToQuery = (previousState) => {
    this.props.history.push({
      pathname: "/app/query",
      state: {
        previousState: previousState ? previousState : this.state,
        routedFrom: this.props.location.pathname,
        routeTargetState: this.state
      }
    });
  };

  format(data) {
    let categories = [];
    data.forEach(element => {
      categories.push({
        id: element.categoryId,
        name: element.categoryName,
        value: element.amount
      });
    });
    return categories;
  }

  render() {
    return (
      <div>
        <Breadcrumb>
          <BreadcrumbItem active>Charts</BreadcrumbItem>
        </Breadcrumb>
        <h1 className="page-title mb-lg">Category Pie</h1>
        <FormGroup className="form-group">
          <Row>
            <Col lg={12}>
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
                            <button
                              className="btn-rounded width-100 mb-xs mr-xs btn btn-outline btn-success"
                              onClick={() => this.clickedFilter()}
                            >
                              <span className="circle bg-white mr-xs">
                                <i className="glyphicon glyphicon-equalizer btn-success"></i>
                              </span>
                              {this.state.isFiltersDirty ? "Chart" : "Filter"}
                            </button>
                              <button
                                className="btn-rounded width-100 mb-xs mr-xs btn btn-outline btn-success"
                                onClick={this.handleRouteToQuery}
                              >
                                <span className="circle bg-white mr-xs">
                                  <i className="glyphicon glyphicon-th-list btn-success"></i>
                                </span>
                                Details
                              </button>
                          </Col>
                        </Row>
                      </FormGroup>
                    </div>
                  </div>
                }
              ></Widget>
            </Col>
          </Row>
          <CardDeck>
          <Card>
              {this.props.categories &&
                this.props.categories.length > 0 &&
                this.props.pieResults &&
                this.state.categoryIds.length > 0 && (
                  <Widget>
                    <div>
                      {this.getCurrentCategoryAncesstors().map(entry => (
                        <span>
                          &nbsp;
                          <button
                            className="btn btn-success"
                            onClick={() => this.handleTreeClick(entry.id)}>
                            {entry.name}
                          </button>
                        </span>
                      ))}
                      <span>
                        &nbsp;
                        <button className="btn btn-primary">
                          {this.getCategory(this.state.categoryIds[0].id).name}
                        </button>
                      </span>
                     
                    </div>
                    <br></br>
                    <SimplePieChart
                      data={this.format(this.props.pieResults)}
                      handleClick={data => this.handleSliceClick(data)}
                    />
                  </Widget>
                )}
            </Card>
            <Card>
              <Widget>
                   {this.props.pieResults && (
                        <div>
                          <br></br>
                          <h3>Total: {numberWithCommas(this.props.pieResults.map(p => p.amount).reduce((a, b) => a + b, 0))}</h3>
                          {this.props.pieResults.filter(p => p.categoryName === 'other').length > 0 && (
                            <h3>Not sorted: {numberWithCommas(this.props.pieResults.filter(p => p.categoryName === 'other').map(p => p.amount)[0])}</h3>
                          )}
                          <br></br>
                        </div>
                      )}
              </Widget>
            </Card>
          </CardDeck>
        </FormGroup>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    isFetching: state.pieResults.isFetching,
    pieResults: state.pieResults.pieResults,
    categories: state.categories.categories,
    accounts: state.accounts.accounts
  };
}

export default connect(mapStateToProps)(CategoryPieChart);
