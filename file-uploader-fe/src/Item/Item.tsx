import React from 'react';
import './Item.css';
import download_icon from "./icons8-download-24.png";
import delete_icon from "./icons8-remove-24.png";

type ItemProps = {
    id: number,
    filename: string,
    size: number,
}

class Item extends React.Component <ItemProps> {
    constructor(props :ItemProps) {
        super(props);
    }
    render () {
        if (this.props.id % 2 === 0) {
            return <div className="Item">
                <div className="Item-Cell-First-Even">{this.props.id}</div>
                <div className="Item-Cell-Name-Even">{this.props.filename}</div>
                <div className="Item-Cell-Size-Even">{this.props.size}</div>
                <div className="Item-Cell-Download-Even">
                    <img src={download_icon} className="Item-Icon" alt="Download FIle"/>
                </div>
                <div className="Item-Cell-Remove-Even">
                    <img src={delete_icon} className="Item-Icon" alt="Delete File"/>
                </div>
            </div>;
        } else {
            return <div className="Item">
                <div className="Item-Cell-First-Odd">{this.props.id}</div>
                <div className="Item-Cell-Name-Odd">{this.props.filename}</div>
                <div className="Item-Cell-Size-Odd">{this.props.size}</div>
                <div className="Item-Cell-Download-Odd">
                    <img src={download_icon} className="Item-Icon" alt="Download FIle"/>
                </div>
                <div className="Item-Cell-Remove-Odd">
                    <img src={delete_icon} className="Item-Icon" alt="Delete File"/>
                </div>
            </div>;
        }
    }
}

export default Item;
