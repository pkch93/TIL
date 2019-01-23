import React from "react";

export default props => 
    (
        <div className={props.styleClass}>
            hello world! {props.name}
        </div>
    );