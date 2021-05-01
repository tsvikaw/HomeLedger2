import React, { PureComponent } from "react";
import {
  Breadcrumb,
  BreadcrumbItem,
  FormGroup,
  Input,
  Label,
  CardDeck,
  Card,
  ButtonGroup
} from "reactstrap";

import { toast } from "react-toastify";

import Widget from "../../components/Widget/Widget";
import {
  fetchCategories,
  newCategory,
  updateCategory,
  deleteCategory
} from "../../actions/categories";

import PropTypes from "prop-types";
import { connect } from "react-redux";

import s from "./CategoryEditor.module.scss";

class CategoryEditor extends PureComponent {
  constructor(props) {
    super(props);

    this.state = {
      category: null,
      editedCategory: null
    };
  }

  static propTypes = {
    message: PropTypes.string,
    category: PropTypes.object, // eslint-disable-line
    categories: PropTypes.array, // eslint-disable-line
    isFetching: PropTypes.bool,
    isDeleted: PropTypes.bool
  };

  static defaultProps = {
    isFetching: false,
    isDeleted: false,
    category: null,
    categories: [],
    accounts: [],
    message: ""
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
        this.props.categories.length,
      categorySaved: !prevProps.category && this.props.category,
      categoryDeleted: !prevProps.isDeleted && this.props.isDeleted,
      failureMessage: !prevProps.message && this.props.message
    };
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    if (snapshot.categoriesLoaded) {
      this.handleCategoriesLoaded();
    }
    if (snapshot.categorySaved) {

      if(this.state.category.id === this.state.editedCategory.id){
        this.setState({category: Object.assign({}, this.state.category , this.state.editedCategory)});
      }
      this.handleCategorySaved();
    }
    if (snapshot.categoryDeleted) {
      this.handleCategorySaved(true);
    }
    if (snapshot.failureMessage) {
      console.error(this.props.message);
      this.showLoadFailureMessage();
    }
  }

  handleCategoriesLoaded() {
    let day2day = this.props.categories.filter(c => c.name === "day to day")[0];
    if (!this.state.category) {
      this.setState({ category: day2day });
    }
  }

  handleCategorySaved(isDeleted) {
    if(isDeleted){
      this.setState({category : this.getCategory(this.state.category.parentId)})
    } 

    this.props.dispatch(fetchCategories(this.state));
    toast.success(
      (isDeleted ? "Deleted" : "Saved") + " " + this.state.editedCategory.name,
      {
        position: "bottom-right",
        autoClose: 5000,
        closeOnClick: true,
        pauseOnHover: false,
        draggable: true
      }
    );

    this.setState({ editedCategory: null });
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

  getCurrentCategoryAncesstors() {
    return this.getCategoryAncesstors(this.state.category.id);
  }

  getCategoryAncesstors(id) {
    let ancesstors = [];
    let current = this.getCategory(id);

    while (current.parentId) {
      ancesstors.push({ id: current.id, name: current.name });
      current = this.getCategory(current.parentId);
    }
    ancesstors.push({ id: current.id, name: current.name });
    ancesstors.reverse();
    ancesstors.pop();
    return ancesstors;
  }

  getCurrentCategoryChildren() {
    return this.getCategoryChildren(this.state.category.id);
  }

  getCategoryChildren(id) {
    return this.props.categories.filter(
      c => c.parentId === id
    );
  }

  getCategory(categoryIdAttribute) {
    return this.props.categories.filter(c => c.id === categoryIdAttribute)[0];
  }

  selectCategory(category, callback) {
    this.setState({ category: category }, callback);
  }

  handleTreeClick(id) {
    this.setState({ category: this.getCategory(id) });
  }

  handleMoveToTreeClick(id) {
    this.setState({ editedCategory: Object.assign({}, this.state.editedCategory, {
      parentId: id
      }) 
    });
    console.log(this.state.editedCategory);
  }

  handleNameChanged = e => {
    this.setState({
      editedCategory: Object.assign({}, this.state.editedCategory, {
        name: e.target.value
      })
    });
  };

  handleDescriptionChanged = e => {
    this.setState({
      editedCategory: Object.assign({}, this.state.editedCategory, {
        description: e.target.value
      })
    });
  };

  handleEnabledChanged = e => {
    this.setState({
      editedCategory: Object.assign({}, this.state.editedCategory, {
        enabled: e.target.checked
      })
    });
  };

  handleNewClicked() {
    this.setState({
      editedCategory: {
        name: "",
        description: "",
        parentId: this.state.category.id,
        enabled: true
      }
    });
  }

  handleEditClicked(id) {
    let category = this.getCategory(id);
    this.setState({
      editedCategory: {
        name: category.name,
        description: category.description,
        id: category.id,
        parentId: category.parentId,
        enabled: category.enabled
      }
    });
  }

  handleMoveClicked(id) {
    let category = this.getCategory(id);
    this.setState({
      editedCategory: {
        name: category.name,
        description: category.description,
        id: category.id,
        parentId: category.parentId,
        enabled: category.enabled,
        moving: true
      }
    });
  }

  handleDeleteClicked(id) {
    let category = this.getCategory(id);
    this.setState({
      editedCategory: {
        name: category.name,
        id: category.id,
        deletion: true
      }
    });
  }

  handleCancelClicked() {
    this.setState({ editedCategory: null });
  }

  handleSaveClicked() {
    this.state.editedCategory.deletion
      ? this.props.dispatch(deleteCategory(this.state.editedCategory.id))
      : this.state.editedCategory.id ? 
        this.props.dispatch(updateCategory(this.state.editedCategory))
      : this.props.dispatch(newCategory(this.state.editedCategory));
  }

  canSave(){
    return (this.state.editedCategory.name && (
    this.state.category.name !== this.state.editedCategory.name ||
    this.state.category.description !== this.state.editedCategory.description ||
    this.state.category.enabled !== this.state.editedCategory.enabled ||
    this.state.category.parentId !== this.state.editedCategory.parentId
    )) ? null : "disabled";
  }

  render() {
    return (
      <div>
        <Breadcrumb>
          <BreadcrumbItem active>Category</BreadcrumbItem>
        </Breadcrumb>
        <h1 className="page-title mb-lg">Category Editor</h1>
        <FormGroup className="form-group">
          <CardDeck>
            <Card>
              {this.props.categories &&
                this.props.categories.length > 0 &&
                this.state.category && (
                  <Widget>
                    <div>
                      {this.getCurrentCategoryAncesstors().map(category => (
                        <span key={category.id}>
                          &nbsp;
                          <button
                            className={"btn btn-inverse " + s.ancesstor}
                            onClick={() => this.handleTreeClick(category.id)}
                          >
                            {category.name}
                          </button>
                        </span>
                      ))}
                      <span>
                        &nbsp;
                        <button className={"btn btn-inverse " + s.ancesstor}>
                          {this.state.category.name}
                          {this.state.category.parentId && !this.state.editedCategory && (
                          <ButtonGroup>
                          &nbsp; |
                          <span
                                  className={s.link}
                                  onClick={() =>
                                    this.handleEditClicked(this.state.category.id)
                                  }
                                >
                                  Edit
                                </span>
                                <span
                                  className={s.link}
                                  onClick={() =>
                                    this.handleMoveClicked(this.state.category.id)
                                  }
                                >
                                  Move
                                </span>
                                <span
                                  className={s.link}
                                  onClick={() =>
                                    this.handleDeleteClicked(this.state.category.id)
                                  }
                                >
                                  Delete
                                </span>
                              </ButtonGroup>
                              )}
                        </button>
                      </span>
                      <span>
                        <ul className={s.childCategories}>
                          {!this.state.editedCategory && (
                            <li>
                                <button
                                  className={"btn btn-inverse " + s.link}
                                  onClick={() => this.handleNewClicked()}
                                >
                                  + New
                                </button>
                            </li>
                          )}
                          {!this.state.editedCategory && this.getCurrentCategoryChildren().map(category => (
                            <li key={category.id}>
                              <button
                                className="btn btn-inverse"
                                onClick={() =>
                                  this.handleTreeClick(category.id)
                                }
                              >
                                {category.name}
                              </button>
                            </li>
                          ))}
                        </ul>
                      </span>
                    </div>
                  </Widget>
                )}
            </Card>
            <Card>
              {this.props.categories &&
                this.state.category &&
                !this.state.editedCategory && (
                  <Widget>
                    <h3>
                      {this.state.category.name}
                      <span className={s.right}></span>
                    </h3>
                    <div>Description: {this.state.category.description}</div>
                    <div>
                      Enabled: {this.state.category.enabled ? "Yes" : "No"}
                    </div>
                  </Widget>
                )}
              {this.props.categories &&
                this.state.category &&
                this.state.editedCategory && (
                  <Widget>
                    {this.state.editedCategory.deletion ? (
                      <div>
                        <h4>Delete {this.state.editedCategory.name} ?</h4>
                      </div>
                    ) : this.state.editedCategory.moving ?
                    (
                      <div>
                        <h4>Moving {this.state.editedCategory.name} to be under {this.getCategory(this.state.editedCategory.parentId).name} </h4>
                        <div>
                      {this.getCategoryAncesstors(this.state.editedCategory.parentId).map(category => (
                        <span key={category.id}>
                          &nbsp;
                          <button
                            className={"btn btn-inverse " + s.ancesstor}
                            onClick={() => this.handleMoveToTreeClick(category.id)}
                          >
                            {category.name}
                          </button>
                        </span>
                      ))}
                      <span>
                        &nbsp;
                        <button className={"btn btn-inverse " + s.ancesstor}>
                          {this.getCategory(this.state.editedCategory.parentId).name}
                        </button>
                      </span>
                      <span>
                        <ul className={s.childCategories}>
                          {this.getCategoryChildren(this.state.editedCategory.parentId).map(category => (
                            <li key={category.id}>
                              <button
                                className="btn btn-inverse"
                                onClick={() =>
                                  this.handleMoveToTreeClick(category.id)
                                }
                              >
                                {category.name}
                              </button>
                            </li>
                          ))}
                        </ul>
                      </span>
                    </div>
                    <br></br>
                      </div>
                    ) : (
                      <div>
                        <h4>
                          {this.state.editedCategory.id
                            ? this.state.editedCategory.name
                            : "New category under " +
                              this.getCategory(this.state.category.id)
                                .name}{" "}
                        </h4>
                        <br></br>
                        <div>
                          Name:
                          <Input
                            type="text"
                            placeholder="Name"
                            className={"form-control"}
                            value={this.state.editedCategory.name}
                            onChange={this.handleNameChanged}
                          />
                        </div>
                        <br></br>
                        <div>
                          Description:
                          <Input
                            type="text"
                            placeholder="Description"
                            className={"form-control"}
                            value={this.state.editedCategory.description}
                            onChange={this.handleDescriptionChanged}
                          />
                        </div>
                        <br></br>
                        <div>
                          Parent:{" "}
                          {this.getCategory(this.state.category.parentId).name}
                        </div>
                        {this.state.editedCategory.id && (
                          <div>
                            <br></br>
                              <Input
                                type="checkbox"
                                className = {s.padLeft}
                                defaultChecked={this.state.editedCategory.enabled}
                                onChange={this.handleEnabledChanged}
                              /> &nbsp; <Label> &nbsp; Enabled</Label> 
                          </div>
                        )}
                        
                        <br></br>
                      </div>
                    )}
                    <ButtonGroup>
                      <button
                        className="btn btn-inverse"
                        onClick={() => this.handleCancelClicked()}
                      >
                        Cancel
                      </button>
                      &nbsp;
                      <button
                        className="btn btn-primary"
                        onClick={() => this.handleSaveClicked()}
                        disabled={this.canSave()}
                      >
                        OK
                      </button>
                    </ButtonGroup>
                  </Widget>
                )}
            </Card>
          </CardDeck>
        </FormGroup>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    message: state.categories.message,
    category: state.categories.category,
    categories: state.categories.categories,
    isFetching: state.categories.isFetching,
    isDeleted: state.categories.isDeleted
  };
}

export default connect(mapStateToProps)(CategoryEditor);
