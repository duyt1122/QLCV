import styles from 'styles/client.module.scss';
import CompanyCard from '@/components/client/card/company.card';
import { Breadcrumb } from 'antd';
import { HomeOutlined } from '@ant-design/icons';
import { Link } from 'react-router-dom';

const ClientCompanyPage = (props: any) => {
    return (
        <div className={styles["container"]} style={{ paddingTop: 24, paddingBottom: 60 }}>
            <Breadcrumb
                style={{ marginBottom: 24 }}
                items={[
                    {
                        title: <Link to="/"><HomeOutlined /> Trang chủ</Link>,
                    },
                    {
                        title: 'Top Công Ty IT',
                    },
                ]}
            />

            <CompanyCard showPagination={true} />
        </div>
    );
};

export default ClientCompanyPage;