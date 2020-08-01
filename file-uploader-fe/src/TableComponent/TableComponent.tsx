import React from 'react';
import DataTable from "react-data-table-component";

const columns = [
    {
        name: 'No',
        selector: 'no',
        center: true,
    },
    {
        name: 'Filename',
        selector: 'filename',
        sortable: true,
    },
    {
        name: 'Size',
        selector: 'size',
        center: true,
    },
    {
        name: 'Options',
        selector: 'options',
        center: true,
    },
];

interface IProps {
}

interface ITableComponentState {
    rows: any,
    pageNumber: number,
    pageSize: number,
    searchPhrase: string,
}

class TableComponent extends React.Component<IProps, ITableComponentState> {

    constructor(props :IProps) {
        super(props);
        this.state = {
            rows: [],
            pageNumber: 0,
            pageSize: 20,
            searchPhrase: "",
        };
    };

    componentDidMount() {
        this.fetchItems();
    }

    fetchItems = (pageNumber :number = 0, searchPhrase:string = '', pageSize :number = 20,
                  sort?: boolean, dir?: ('asc' | 'desc')) => {
        const url = process.env.REACT_APP_API_URL + "/api/file?phrase="+searchPhrase+"&page="+pageNumber+ "&size="+pageSize +
            (sort ? "&sort=filename," + dir : "");
        fetch(url)
            .then(res => res.json())
            .then(result => {
                const startIndex = result.size*result.number;
                return result.content.map((item:any, index:number) => {
                        return {
                            no: startIndex + index + 1,
                            filename: item.filename,
                            size: this.formatSize(item.size),
                            options: "Usuń",
                        };
                });
            })
            .then(result => this.setState({rows: result}))
            .catch((error) => {
                console.log(error);
            })
    };

    onSort = (dir: ('asc' | 'desc')) => {
        return this.fetchItems();
    }

    formatSize = (size :number) => {
        const units = ['B', 'KB', 'MB', 'GB', 'TB', 'PB'];
        let units_index = 0;
        while (size > 1024) {
            size /= 1024;
            units_index++;
        }
        return size.toFixed(1) + ' ' + units[units_index];
    };

    render = () => <div className={"tableComponent"}>
                        <DataTable
                            columns={columns}
                            data={this.state.rows}
                            /*sortFunction={(rows,field,dir) => this.onSort(dir) }*/
                        />
                    </div>;

}

export default TableComponent;
