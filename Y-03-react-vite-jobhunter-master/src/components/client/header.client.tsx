import { useState, useEffect } from 'react';
import { 
    CodeOutlined, 
    ContactsOutlined, 
    FireOutlined, 
    LogoutOutlined, 
    MenuFoldOutlined, 
    RiseOutlined, 
    ThunderboltFilled,
    AppstoreOutlined,
    UserOutlined
} from '@ant-design/icons';
import { Avatar, Drawer, Dropdown, MenuProps, Space, message, Button } from 'antd';
import { Menu } from 'antd';
import styles from '@/styles/client.module.scss';
import { isMobile } from 'react-device-detect';
import { useLocation, useNavigate, Link } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from '@/redux/hooks';
import { callLogout } from '@/config/api';
import { setLogoutAction } from '@/redux/slice/accountSlide';
import ManageAccount from './modal/manage.account';

const Header = (props: any) => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();

    const isAuthenticated = useAppSelector(state => state.account.isAuthenticated);
    const user = useAppSelector(state => state.account.user);
    const [openMobileMenu, setOpenMobileMenu] = useState<boolean>(false);

    const [current, setCurrent] = useState('home');
    const location = useLocation();

    const [openManageAccount, setOpenManageAccount] = useState<boolean>(false);

    useEffect(() => {
        setCurrent(location.pathname);
    }, [location]);

    const items: MenuProps['items'] = [
        {
            label: <Link to={'/'}>Trang Chủ</Link>,
            key: '/',
            icon: <AppstoreOutlined />,
        },
        {
            label: <Link to={'/job'}>Việc Làm IT</Link>,
            key: '/job',
            icon: <CodeOutlined />,
        },
        {
            label: <Link to={'/company'}>Top Công Ty IT</Link>,
            key: '/company',
            icon: <RiseOutlined />,
        }
    ];

    const onClick: MenuProps['onClick'] = (e) => {
        setCurrent(e.key);
    };

    const handleLogout = async () => {
        const res = await callLogout();
        if (res && +res.statusCode === 200) {
            dispatch(setLogoutAction({}));
            message.success('Đăng xuất thành công');
            navigate('/');
        }
    };

    const itemsDropdown: MenuProps['items'] = [
        {
            label: (
                <span onClick={() => setOpenManageAccount(true)} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                    <ContactsOutlined /> Quản lý tài khoản
                </span>
            ),
            key: 'manage-account',
        },
        ...(user.role?.permissions?.length ? [{
            label: (
                <Link to={"/admin"} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                    <FireOutlined style={{ color: '#f59e0b' }} /> Trang Quản Trị
                </Link>
            ),
            key: 'admin',
        }] : []),
        {
            type: 'divider',
        },
        {
            label: (
                <span onClick={() => handleLogout()} style={{ display: 'flex', alignItems: 'center', gap: 8, color: '#ef4444' }}>
                    <LogoutOutlined /> Đăng xuất
                </span>
            ),
            key: 'logout',
        },
    ];

    const itemsMobiles = [...items, ...itemsDropdown];

    return (
        <>
            <header className={styles["header-section"]}>
                <div className={styles["container"]}>
                    {!isMobile ? (
                        <div className={styles["header-inner"]}>
                            {/* Brand Logo */}
                            <div className={styles['brand']} onClick={() => navigate('/')}>
                                <div className={styles['brand-icon']}>
                                    <ThunderboltFilled />
                                </div>
                                <div className={styles['brand-text']}>
                                    <div className={styles['brand-name']}>
                                        Dev<span>Hire</span>
                                    </div>
                                    <div className={styles['brand-tagline']}>Tech Career Platform</div>
                                </div>
                            </div>

                            {/* Navigation Menu */}
                            <div className={styles['top-menu']}>
                                <Menu
                                    onClick={onClick}
                                    selectedKeys={[current]}
                                    mode="horizontal"
                                    items={items}
                                />

                                {/* Auth Actions */}
                                <div className={styles['extra']}>
                                    {!isAuthenticated ? (
                                        <>
                                            <Link to={'/login'} className={styles['login-btn']}>
                                                Đăng Nhập
                                            </Link>
                                            <Link to={'/register'} className={styles['register-btn']}>
                                                Đăng Ký Ngay
                                            </Link>
                                        </>
                                    ) : (
                                        <Dropdown menu={{ items: itemsDropdown }} trigger={['click']} placement="bottomRight">
                                            <div className={styles['user-pill']}>
                                                <Avatar 
                                                    style={{ 
                                                        backgroundColor: '#4f46e5',
                                                        verticalAlign: 'middle',
                                                        boxShadow: '0 0 0 2px rgba(255,255,255,0.2)' 
                                                    }}
                                                    icon={<UserOutlined />}
                                                >
                                                    {user?.name?.substring(0, 2)?.toUpperCase()}
                                                </Avatar>
                                                <span>{user?.name}</span>
                                            </div>
                                        </Dropdown>
                                    )}
                                </div>
                            </div>
                        </div>
                    ) : (
                        <div className={styles['header-mobile']}>
                            <div className={styles['brand']} onClick={() => navigate('/')}>
                                <div className={styles['brand-icon']}>
                                    <ThunderboltFilled />
                                </div>
                                <span className={styles['mobile-brand']}>DevHire</span>
                            </div>
                            <MenuFoldOutlined onClick={() => setOpenMobileMenu(true)} />
                        </div>
                    )}
                </div>
            </header>

            <Drawer 
                title="Menu Điều Hướng"
                placement="right"
                onClose={() => setOpenMobileMenu(false)}
                open={openMobileMenu}
            >
                <Menu
                    onClick={onClick}
                    selectedKeys={[current]}
                    mode="vertical"
                    items={itemsMobiles}
                />
            </Drawer>

            <ManageAccount
                open={openManageAccount}
                onClose={setOpenManageAccount}
            />
        </>
    );
};

export default Header;