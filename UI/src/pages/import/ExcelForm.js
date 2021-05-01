import React from "react";
import PropTypes from "prop-types";
import {Table, Input} from "reactstrap";

const ExcelForm = ({
  excelLines,
  categories,
  onSave,
  onChange,
  onDelete,
  onSelectRecommended,
  saving = false,
  errors = {},
  hideIrrelevant
}) => {
  return (
    <form onSubmit={onSave}>
      {errors.onSave && (
        <div className="alert alert-danger" role="alert">
          {errors.onSave}
        </div>
      )}
       <div className="table-responsive">
                <Table className="table-hover">
        <tbody>
          <tr>
            <th>Status</th>
            <th>Date</th>
            <th>Description</th>
            <th>Amount</th>
            <th>Suggestions</th>
          </tr>
          {excelLines.map((line, index) => {

            return (!line.previouselyImported || 
              ((line.previouselyImported || line.isDeleted) && !hideIrrelevant)) && (
              <tr key={index} className={line.isDeleted || line.previouselyImported ? "" :
              line.line.categoryId ? "bg-success" :
               line.categoryHits && line.categoryHits.length ? "bg-warning" :
                "bg-light"}>
                <td>
                     {line.isDeleted ? (<div>Deleted</div>) :
                      line.previouselyImported ? (<div>Already Saved</div>) :
                 line.line.categoryId ? (<div>Selected</div>) :
                  line.categoryHits && line.categoryHits.length ? (<div>Recommended&nbsp;
                    <button onClick={e => onSelectRecommended(line, e)} className="bg-success">
                      select</button>
                  </div>) :
                  (<div>Open</div>)}
                </td>
                <td>
                  {line.line.date.split("T")[0]}
                </td>
                <td>
                    {line.line.description}
                </td>
                <td>
                    {line.line.amount.toFixed(2)}
                </td>
                <td> {!line.isDeleted && !line.previouselyImported && (
                    <Input
                    type="select"
                    name="category"
                    className="form-control input-sm"
                    value={line.line.categoryId}
                    onChange={e => onChange(line, e)}
                  >
                    {([
                      ...(line.previouselyImported ? [] : line.categoryHits.map(category => ({
                        value: category.category.id,
                        text: category.category.name + "(" + category.hits + " previous matches)"
                      }))),
                      ...categories.map(category => ({
                        value: category.id,
                        text: category.name
                      }))
                    ]).map((cat) => { return cat &&
                      (<option key={cat.value+cat.text} value={cat.value}>{cat.text}</option>);
                    })}
                  </Input>
                )}
                </td>
                <td>{!line.isDeleted && !line.previouselyImported &&  (
                  <button className="btn btn-warning" onClick={e => onDelete(line, e)}>Delete</button>
                )}
                </td>
              </tr>
            );
          })}
        </tbody>
      </Table>
      </div>
      <br></br>
      <h2>Summary</h2>
      <Table>
        <tbody>
          <tr>
            <td>Set</td>
            <td>{excelLines.filter(l => l.line.categoryId !== '' && !l.previouselyImported).length}</td>
          </tr>
          <tr>
            <td>Not set</td>
            <td>{excelLines.filter(l => !l.isDeleted && l.line.categoryId === '').length}</td>
          </tr>
          <tr>
            <td>Deleted</td>
            <td>{excelLines.filter(l => l.isDeleted).length}</td>
          </tr>
          <tr>
            <td>Already imported</td>
            <td>{excelLines.filter(l => l.previouselyImported).length}</td>
          </tr>
          <tr>
            <td>Total</td>
            <td>{excelLines.length}</td>
          </tr>
        </tbody>
      </Table>

      <button type="submit" disabled={saving || 
      excelLines.filter(l => !l.isDeleted && !l.previouselyImported && l.line.categoryId === '').length} 
        className="btn btn-primary">
        {saving ? "Saving..." : "Save"}
      </button>
    </form>
  );
};

ExcelForm.propTypes = {
  categories: PropTypes.array.isRequired,
  excelLines: PropTypes.array.isRequired,
  errors: PropTypes.object,
  hideIrrelevant: PropTypes.bool,
  onSave: PropTypes.func.isRequired,
  onChange: PropTypes.func.isRequired,
  onSelectRecommended: PropTypes.func.isRequired,
  saving: PropTypes.bool
};

export default ExcelForm;
