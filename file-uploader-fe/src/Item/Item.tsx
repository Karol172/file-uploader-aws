import React from 'react';
import './Item.css';
import download_icon from "./icons8-download-24.png";
import delete_icon from "./icons8-remove-24.png";

type ItemProps = {
    id: string,
    filename: string,
    size: string,
}
class Item extends React.Component <ItemProps> {
    constructor(props :ItemProps) {
        super(props);
    }
    render () {
        return <div className="Item">
            <div className="Item-Cell-First">{this.props.id}</div>
            <div className="Item-Cell-Name">{this.props.filename}</div>
            <div className="Item-Cell-Size">{this.props.size}</div>
            <div className="Item-Cell-Download">
                <img src={download_icon} className="Item-Icon" alt="Download FIle" />
            </div>
            <div className="Item-Cell-Remove">
                <img src={delete_icon} className="Item-Icon" alt="Delete File" />
            </div>
        </div>;
    }
}

export default Item;
