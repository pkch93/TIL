import React from "react";
import Box from './Box';

export default props => {
    const styleClass = "square col-12 col-md-4 col-lg-2 mt-3";
    return (
        <div className="row">
            {props.names.map((name, i) => <Box key={i} name={name} styleClass={styleClass}/>)}
        </div>
    );
};