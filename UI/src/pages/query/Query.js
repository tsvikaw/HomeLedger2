import React, { Component } from "react";
import {
  Row,
  Col,
  Table,
  Breadcrumb,
  BreadcrumbItem,
  Button,
  FormGroup,
  Input
} from "reactstrap";

import Widget from "../../components/Widget/Widget";
import { toast } from 'react-toastify';

import { fetchQueryResults } from "../../actions/queryResults";
import { fetchCategories } from "../../actions/categories";
import { fetchAccounts } from "../../actions/accounts";
import { updateLines } from "../../actions/lines";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { pad } from "../../utils/stringUtils";
import { formatDate } from "../../utils/dateUtils";
import DropdownTreeSelect from "react-dropdown-tree-select";
import "react-dropdown-tree-select/dist/styles.css";
import { YearSelector, MonthSelector } from "./Filters";
import { PageSizeSelector, PageSelector, PageRangeDisplayer } from "./Pager";
import { getCategoryImage, getAccountImage } from "./ImageMapper";

class Query extends Component {
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
      endAmount: 10000,
      account: null,
      categoryIds: [],
      pageSize: 10,
      pageNumber: 1,
      sortBy: "date",
      sortAsc: false,
      checked: [],
      updatedCategoryId: null,
    };

    this.pageTo = this.pageTo.bind(this);
  }

  static propTypes = {
    dispatch: PropTypes.func.isRequired,
    queryResults: PropTypes.object, // eslint-disable-line
    categories: PropTypes.array, // eslint-disable-line
    accounts: PropTypes.array, // eslint-disable-line
    updatedLines: PropTypes.array, // eslint-disable-line
    isFetching: PropTypes.bool
  };

  static defaultProps = {
    isFetching: false,
    queryResults: null,
    categories: [],
    accounts: [],
    updatedLines: [],
  };

  componentDidMount() {
    this.props.dispatch(fetchCategories());
    this.props.dispatch(fetchAccounts());
  }

  getSnapshotBeforeUpdate(prevProps) {
    return {categoryUpdated: (!prevProps.updatedLines || !prevProps.updatedLines.length) 
      && this.props.updatedLines && this.props.updatedLines.length ,
        categoriesLoaded: (!prevProps.categories || !prevProps.categories.length) 
        && this.props.categories && this.props.categories.length}
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    if(snapshot.categoriesLoaded){
      this.handleCategoriesLoaded();
    }

    if(snapshot.categoryUpdated){
      this.handleCategoryUpdated();
    }
  }

  handleCategoryUpdated() {
    this.props.dispatch(fetchQueryResults(this.state));
    this.setState({ checked: [] });
    toast.success(this.props.updatedLines.length + ' lines updated successfuly', {
      position: "bottom-right",
      autoClose: 5000,
      closeOnClick: true,
      pauseOnHover: false,
      draggable: true
    });
  }

  handleCategoriesLoaded() {
    if(this.props.location.state){
      this.setStateFromRoute(this.props.location.state.routeTargetState);
    } else {
      let day2day = this.props.categories.filter(c => c.name === "day to day")[0];
      this.selectCategory(day2day);
      this.props.dispatch(fetchQueryResults(this.state));
    }
  }

  setStateFromRoute(stateFromRoute){
    this.setState({
      description: stateFromRoute.description,
      startYear: stateFromRoute.startYear,
      startMonth: stateFromRoute.startMonth,
      endYear: stateFromRoute.endYear,
      endMonth: stateFromRoute.endMonth,
      account: stateFromRoute.account,
      categoryIds: stateFromRoute.categoryIds
    }, ()=> this.props.dispatch(fetchQueryResults(this.state)));
  }

  getCategoryTreeRootNode(){
    if(!this.props.categories || !this.props.categories.length){
      return null;
    }
    let root = this.props.categories.filter(c => c.name === "root")[0];
    return this.createNode(root);
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

  doFilter() {
    this.props.dispatch(fetchQueryResults(this.state));
  }

  clickedUpdateLinesCategory(){
    this.props.dispatch(updateLines(this.state.updatedCategoryId, this.state.checked));
  }

  clickedDescriptionLink(description) {
    this.setState({ description: description }, this.doFilter);
  }

  clickedCategoryLink(categoryId) {
    const category = this.props.categories.filter(c => c.id === categoryId)[0];
    this.selectCategory(category, this.doFilter);
  }

  selectCategory(category) {
    let selectedCategories = [];
    selectedCategories.push({ id: category.id });
    this.setState({categoryIds : selectedCategories});
  }

  clickedAccountLink(accountId) {
    this.setState({ account: accountId }, this.doFilter);
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
      selectedNodes.forEach(sn => {
        let category = this.props.categories.filter(c => c.id === sn.value)[0];
        this.selectCategory(category);
      });
    }
  };

  handleAccountChange = e => {
    this.setState({
      account: e.target.value === "default" ? null : e.target.value
    });
  };

  handleUpdatedCategoryChange = e => {
    this.setState({ updatedCategoryId: e.target.value});
  };

  handleStartAmountChange = e => {
    this.setState({ startAmount: e.target.value });
  };

  handleEndAmountChange = e => {
    this.setState({ endAmount: e.target.value });
  };

  handleChangePageSize = e => {
    this.setState({ pageSize: e.target.value }, this.doFilter);
  };

  handleChangeCheck = (id, categoryId, e) => {
    let newChecked = this.state.checked;

    if(this.state.checked.indexOf(id) === -1){
      if(e.target.checked){
        newChecked.push(id);
        this.setState({ checked: newChecked,  updatedCategoryId: categoryId });
      }
    } else {
      if(!e.target.checked){
        newChecked = newChecked.filter(lid => lid !== id);
        this.setState({ checked: newChecked });
      }
    }
  };

  sort(sortBy) {
    if (sortBy === this.state.sortBy) {
      this.setState({ sortAsc: !this.state.sortAsc }, this.doFilter);
    } else {
      this.setState({ sortBy: sortBy }, this.doFilter);
    }
  }

  pageTo(page) {
    this.setState({ pageNumber: page }, this.doFilter);
  }

  backToChart(){
    this.props.history.push({pathname:this.props.location.state.routedFrom, state:{previousState: this.props.location.state.previousState}});
  }

  render() {
    return (
      <div>
        <Breadcrumb>
          {this.props.location.state && (
            <BreadcrumbItem active>
            <button className="btn btn-success"
            onClick={() => this.backToChart()}>
            &lt;&lt; Back to Chart
            </button>
            </BreadcrumbItem>
          )}
        </Breadcrumb>
        <h1 className="page-title mb-lg">
          <span className="fw-semi-bold">Expence</span> Details
        </h1>
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
                        <Col lg={1}>
                          <Input
                            type="number"
                            placeholder="From amount..."
                            className="form-control input-sm"
                            value={this.state.startAmount}
                            onChange={this.handleStartAmountChange}
                          />
                          <br></br>
                          <Input
                            type="number"
                            placeholder="To amount..."
                            className="form-control input-sm"
                            value={this.state.endAmount}
                            onChange={this.handleEndAmountChange}
                          />
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
                            onClick={() => this.doFilter()}
                          >
                            <span className="circle bg-white mr-xs">
                              <i className="glyphicon glyphicon-filter btn-success"></i>
                            </span>
                            Filter
                          </button>
                          {this.state.checked.length >0 && (
                            <button className="btn-rounded width-100 mb-xs mr-xs btn btn-outline btn-warning" 
                              onClick={() => this.clickedUpdateLinesCategory()}>
                              Update
                            </button>)}
                        </Col>
                      </Row>
                    </FormGroup>
                  </div>
                </div>
              }
            >
              <PageSizeSelector
                id="pageSizeSelect"
                pageSize={this.state.pageSize}
                handleChange={this.handleChangePageSize}
              />
              <p className="label">
                {this.props.queryResults &&
                  this.props.queryResults.total > 0 && (
                    <PageRangeDisplayer
                      pageNumber={this.state.pageNumber}
                      pageSize={this.state.pageSize}
                      total={this.props.queryResults.total}
                    />
                  )}
              </p>
              <div className="table-responsive">
                <Table className="table-hover">
                  <thead>
                    <tr>
                      <th>
                      </th>
                      <th onClick={() => this.sort("date")}>
                        Date{" "}
                        {this.state.sortBy === "date" && (
                          <i className="glyphicon glyphicon-sort"></i>
                        )}
                      </th>
                      <th
                        onClick={() => this.sort("amount")}
                        className="text-right"
                      >
                        Amount{" "}
                        {this.state.sortBy === "amount" && (
                          <i className="glyphicon glyphicon-sort"></i>
                        )}
                      </th>
                      <th onClick={() => this.sort("categoryId")}>
                        Category{" "}
                        {this.state.sortBy === "categoryId" && (
                          <i className="glyphicon glyphicon-sort"></i>
                        )}
                      </th>
                      <th
                        onClick={() => this.sort("description")}
                        className="text-right"
                      >
                        Description{" "}
                        {this.state.sortBy === "description" && (
                          <i className="glyphicon glyphicon-sort"></i>
                        )}
                      </th>
                      <th onClick={() => this.sort("account")}>
                        Account{" "}
                        {this.state.sortBy === "account" && (
                          <i className="glyphicon glyphicon-sort"></i>
                        )}
                      </th>
                    </tr>
                  </thead>
                  {/* eslint-disable */}
                  <tbody>
                    {this.props.queryResults &&
                      this.props.queryResults.items &&
                      this.props.queryResults.items.map(queryResult => (
                        <tr key={queryResult.id}>
                          <td>
                            <input type="checkbox" onChange={(e) => this.handleChangeCheck(queryResult.id, queryResult.categoryId, e)}></input>
                          </td>
                          <td className="label-mid-vertical">
                            {formatDate(new Date(queryResult.date))}
                          </td>
                          <td className="text-right label-mid-vertical">
                            {queryResult.amount.toFixed(2)}
                          </td>
                          <td>
                            {this.state.checked.indexOf(queryResult.id) === -1 ? (
                              <button
                              className="mr-xs btn btn-default"
                              onClick={() =>
                                this.clickedCategoryLink(queryResult.categoryId)
                              }
                            >
                              <i
                                className={'label-mid-vertical text-primary mr-xs mb-xs ' + getCategoryImage(
                                  queryResult.categoryName
                                )}
                              ></i>
                              {queryResult.categoryName}
                            </button>
                            ) : (
                              <Input
                              type="select"
                              name="selectCategory"
                              className="form-control input-sm"
                              id="categorySelect"
                              value={this.state.updatedCategoryId || ""}
                              onChange={this.handleUpdatedCategoryChange}
                            >
                              <option key="0" value="default">
                                Select category
                              </option>
                              {this.props.categories &&
                                this.props.categories.map(category => (
                                  <option key={category.id} value={category.id}>
                                    {category.name}
                                  </option>
                                ))}
                            </Input>
                            )}
                          </td>
                          <td className="text-right">
                            <Button
                              onClick={() =>
                                this.clickedDescriptionLink(
                                  queryResult.description
                                )
                              }
                              className="mr-xs"
                            >
                              {queryResult.description}
                            </Button>
                          </td>
                          <td>
                            <button
                              className="mr-xs btn btn-default"
                              onClick={() =>
                                this.clickedAccountLink(queryResult.account)
                              }
                            >
                              <i
                                className={'label-mid-vertical text-primary mr-xs mb-xs ' + getAccountImage(queryResult.account)}
                              ></i>
                              {queryResult.account}
                            </button>
                          </td>
                        </tr>
                      ))}
                    {this.props.isFetching && (
                      <tr>
                        <td colSpan="100">Loading...</td>
                      </tr>
                    )}
                  </tbody>
                  {/* eslint-enable */}
                </Table>
                {this.props.queryResults &&
                  this.props.queryResults.total > 0 && (
                    <PageSelector
                      page={this.props.queryResults.pageNumber + 1}
                      pageSize = {this.props.queryResults.pageSize}
                      total = {this.props.queryResults.total}
                      pageTo={this.pageTo}
                    />
                  )}
              </div>
            </Widget>
          </Col>
        </Row>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    isFetching: state.queryResults.isFetching,
    queryResults: state.queryResults.queryResults,
    categories: state.categories.categories,
    accounts: state.accounts.accounts,
    updatedLines: state.lines.lines
  };
}

export default connect(mapStateToProps)(Query);
