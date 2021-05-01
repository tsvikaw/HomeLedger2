import React from "react";
import {Input} from "reactstrap";

export const YearSelector = (props) => {
    return (
      <Input
          type="select"
          name="select"
          className="form-control input-sm"
          id={props.id}
          value={props.year}
          onChange={props.handleChange}
        >
          <option>{new Date().getFullYear()}</option>
          <option>{new Date().getFullYear() - 1}</option>
          <option>{new Date().getFullYear() - 2}</option>
          <option>{new Date().getFullYear() - 3}</option>
          <option>{new Date().getFullYear() - 4}</option>
          <option>{new Date().getFullYear() - 5}</option>
          <option>{new Date().getFullYear() - 6}</option>
          <option>{new Date().getFullYear() - 7}</option>
          <option>{new Date().getFullYear() - 8}</option>
        </Input>
    );
  }

  export const MonthSelector = (props) => {
    return (
        <Input
        type="select"
          name="select"
          className="form-control input-sm"
          id={props.id}
          value={props.month}
          onChange={props.handleChange}
      >
        <option>01</option>
        <option>02</option>
        <option>03</option>
        <option>04</option>
        <option>05</option>
        <option>06</option>
        <option>07</option>
        <option>08</option>
        <option>09</option>
        <option>10</option>
        <option>11</option>
        <option>12</option>
      </Input>
    );
  }