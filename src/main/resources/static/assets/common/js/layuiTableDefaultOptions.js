let layuiTableOptions = {
    page: true,
    defaultToolbar: [],
    limit: 10,
    limits: [10, 50, 100],
    loading: true,
    size: 'lg',
    autoSort: false,
    method: 'post',
    request: {
        pageName: 'pageNumber',
        limitName: 'pageSize'
    }
};