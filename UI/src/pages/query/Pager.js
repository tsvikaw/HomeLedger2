import React from "react";
import { Input } from "reactstrap";

export const PageSizeSelector = props => {
  return (
    <div className="float-right btn-group">
      <span className="label btn">Show</span>
      <Input
        type="select"
        name="selectPageSize"
        className="form-control input-sm"
        id={props.id}
        value={props.pageSize}
        onChange={props.handleChange}
      >
        <option>10</option>
        <option>25</option>
        <option>50</option>
        <option>100</option>
        <option>200</option>
      </Input>
    </div>
  );
};


const lastPageNumber = (total, pageSize) => {
    if(!total || !pageSize){
        return 1;
    }

    return (
      Math.floor(
        total / pageSize
      )
    ) + ((total % pageSize) ? 1 : 0);
  }

export const PageSelector = props => {
    const lastPage = lastPageNumber(props.total, props.pageSize);
  return (
    <div className="float-right">
      <span className="btn-group">
        <span className="label-mid-vertical">Page</span>&nbsp;
        <span className="label" onClick={() => props.pageTo(1)}>
          <span className="glyphicon glyphicon-fast-backward"></span>
        </span>
        &nbsp;
        <span className="label" onClick={() => props.pageTo(props.page - 1)}>
          <span className="glyphicon glyphicon-step-backward"></span>
        </span>
        &nbsp;
        <Input
          type="number"
          value={props.page}
          min="1"
          max={lastPage}
          onChange={e => props.pageTo(e.target.value)}
          className="form-control input-sm"
        ></Input>
      </span>
      &nbsp;
      <span className="label" onClick={() => props.pageTo(props.page + 1)}>
        <span className="label glyphicon glyphicon-step-forward"></span>
      </span>
      &nbsp;
      <span
        className="label"
        onClick={() => props.pageTo(lastPage)}
      >
        <span className="label glyphicon glyphicon-fast-forward"></span>
      </span>
      <span className="btn-group">
        <span className="label-mid-vertical btn">
          of {lastPage}
        </span>
      </span>
    </div>
  );
};

export const PageRangeDisplayer = props => {
  return (
    <span>
      {(props.pageNumber - 1) * props.pageSize + 1} -{" "}
      {Math.min(props.total, props.pageNumber * props.pageSize)} of{" "}
      {props.total}
    </span>
  );
};
