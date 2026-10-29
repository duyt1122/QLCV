import { callFetchCompany } from '@/config/api';
import { convertSlug } from '@/config/utils';
import { ICompany } from '@/types/backend';
import { Col, Empty, Pagination, Row, Spin } from 'antd';
import { ArrowRightOutlined, BankOutlined, EnvironmentOutlined } from '@ant-design/icons';
import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styles from 'styles/client.module.scss';

interface IProps {
    showPagination?: boolean;
}

const CompanyCard = (props: IProps) => {
    const { showPagination = false } = props;

    const [displayCompany, setDisplayCompany] = useState<ICompany[] | null>(null);
    const [isLoading, setIsLoading] = useState<boolean>(false);

    const [current, setCurrent] = useState(1);
    const [pageSize, setPageSize] = useState(4);
    const [total, setTotal] = useState(0);
    const [filter, setFilter] = useState("");
    const [sortQuery, setSortQuery] = useState("sort=updatedAt,desc");
    const navigate = useNavigate();

    useEffect(() => {
        fetchCompany();
    }, [current, pageSize, filter, sortQuery]);

    const fetchCompany = async () => {
        setIsLoading(true);
        let query = `page=${current}&size=${pageSize}`;
        if (filter) {
            query += `&${filter}`;
        }
        if (sortQuery) {
            query += `&${sortQuery}`;
        }

        const res = await callFetchCompany(query);
        if (res && res.data) {
            setDisplayCompany(res.data.result);
            setTotal(res.data.meta.total);
        }
        setIsLoading(false);
    };

    const handleOnchangePage = (pagination: { current: number, pageSize: number }) => {
        if (pagination && pagination.current !== current) {
            setCurrent(pagination.current);
        }
        if (pagination && pagination.pageSize !== pageSize) {
            setPageSize(pagination.pageSize);
            setCurrent(1);
        }
    };

    const handleViewDetailCompany = (item: ICompany) => {
        if (item.name) {
            const slug = convertSlug(item.name);
            navigate(`/company/${slug}?id=${item.id}`);
        }
    };

    return (
        <div className={styles["company-section"]}>
            {/* Header */}
            <div className={styles["section-header"]}>
                <div className={styles["section-title-wrap"]}>
                    <span className={styles["section-tag"]}>DOANH NGHIỆP UY TÍN</span>
                    <h2 className={styles["section-title"]}>Top Công Ty IT Hàng Đầu</h2>
                </div>
                {!showPagination && (
                    <Link to="/company" className={styles["view-all-link"]}>
                        Xem tất cả {total > 0 ? `(${total})` : ''} <ArrowRightOutlined />
                    </Link>
                )}
            </div>

            <Spin spinning={isLoading} tip="Đang tải danh sách công ty...">
                <Row gutter={[20, 20]}>
                    {displayCompany?.map(item => {
                        return (
                            <Col span={24} sm={12} md={6} key={item.id}>
                                <div 
                                    className={styles["company-card"]}
                                    onClick={() => handleViewDetailCompany(item)}
                                >
                                    <div className={styles["card-cover"]}>
                                        <div className={styles["banner-overlay"]} />
                                    </div>
                                    <div className={styles["card-body"]}>
                                        <div className={styles["logo-wrap"]}>
                                            {item.logo ? (
                                                <img
                                                    alt={item.name}
                                                    src={`${import.meta.env.VITE_BACKEND_URL}/storage/company/${item.logo}`}
                                                    onError={(e: any) => {
                                                        e.target.onerror = null;
                                                        e.target.src = 'https://placehold.co/100x100/4f46e5/ffffff?text=Company';
                                                    }}
                                                />
                                            ) : (
                                                <BankOutlined style={{ fontSize: 32, color: '#4f46e5' }} />
                                            )}
                                        </div>

                                        <h3 className={styles["comp-name"]} title={item.name}>
                                            {item.name}
                                        </h3>

                                        <span className={styles["comp-badge"]}>
                                            Top Tuyển Dụng
                                        </span>

                                        <div style={{ fontSize: 13, color: '#64748b', marginBottom: 12, display: 'flex', alignItems: 'center', gap: 4 }}>
                                            <EnvironmentOutlined style={{ color: '#0ea5e9' }} />
                                            <span style={{ overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', maxWidth: 180 }}>
                                                {item.address || 'Hà Nội & TP. HCM'}
                                            </span>
                                        </div>

                                        <div className={styles["comp-action"]}>
                                            Xem chi tiết & việc làm <ArrowRightOutlined style={{ fontSize: 11 }} />
                                        </div>
                                    </div>
                                </div>
                            </Col>
                        );
                    })}

                    {(!displayCompany || displayCompany.length === 0) && !isLoading && (
                        <Col span={24}>
                            <div style={{ padding: '60px 0', textAlign: 'center', background: '#fff', borderRadius: 16 }}>
                                <Empty description="Hiện chưa có dữ liệu công ty." />
                            </div>
                        </Col>
                    )}
                </Row>

                {showPagination && total > 0 && (
                    <div style={{ marginTop: 36, display: 'flex', justifyContent: 'center' }}>
                        <Pagination
                            current={current}
                            total={total}
                            pageSize={pageSize}
                            responsive
                            showSizeChanger={false}
                            onChange={(p: number, s: number) => handleOnchangePage({ current: p, pageSize: s })}
                        />
                    </div>
                )}
            </Spin>
        </div>
    );
};

export default CompanyCard;