import React from 'react';
import DataTable, {IDataTableProps, IDataTableColumn} from 'react-data-table-component'

const columns = [
    {
        name: 'No',
        selector: 'no',
        sortable: false,
    },
    {
        name: 'Filename',
        selector: 'filename',
        sortable: true,
    },
    {
        name: 'Size',
        selector: 'size',
        sortable: false,
    },
    {
        name: 'Options',
        selector: 'options',
        sortable: false,
    },
];

type IProps = {
};

type TableComponentState = {
    data: any,
    page: number,
    pageSize: number,
    phrase: string,
};

class TableComponent extends React.Component<IProps, TableComponentState> {

    constructor(props :IProps) {
        super(props);
        this.state = {data: [], page: 0, pageSize: 20, phrase: ""};
    }

    componentDidMount() {
        this.listItem(this.state.phrase, this.state.page, this.state.pageSize);
    }

    listItem = (phrase: string, page: number, size: number) => {
        fetch("http://localhost:8080/api/file?phrase="+phrase+
            "&page=" + this.state.page + "&size=" + this.state.pageSize)
            .then(res => res.json())
            .then(result => {
                const startIndex = result.size*result.number;
                this.setState({
                    page : result.pageNumber,
                    pageSize: result.pageSize,
                    data: result.content.map((item:any, index:number) => { return {
                        no: startIndex + index + 1,
                        filename: item.filename,
                        size: this.formatSize(item.size),
                        options: "Usuń",
                    };}),
                })
            }, (error) => {})
    }

    formatSize = (size :number) => {
        console.log(size);
        const units = ['B', 'KB', 'MB', 'GB', 'TB', 'PB'];
        let units_index = 0;
        while (size > 1024) {
            size /= 1024;
            units_index++;
        }
        return size.toFixed(1) + ' ' + units[units_index];
    }

    render = () => <div className={"tableComponent"}>
                        <DataTable
                            columns={columns}
                            data={this.state.data}
                            pagination
                            onChangePage={(page, totalRows) =>
                                this.listItem(this.state.phrase, page, this.state.pageSize)}
                        />
                    </div>;

}

export default TableComponent;
