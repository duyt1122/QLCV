import styles from '@/styles/client.module.scss';
import { 
    ThunderboltFilled, 
    GithubOutlined, 
    LinkedinOutlined, 
    FacebookOutlined, 
    TwitterOutlined,
    MailOutlined,
    PhoneOutlined,
    EnvironmentOutlined
} from '@ant-design/icons';
import { Link } from 'react-router-dom';

const Footer = () => {
    return (
        <footer className={styles['footer-section']}>
            <div className={styles['container']}>
                <div className={styles['footer-grid']}>
                    {/* Brand column */}
                    <div className={styles['footer-brand-col']}>
                        <div className={styles['footer-brand']}>
                            <div className={styles['footer-icon']}>
                                <ThunderboltFilled />
                            </div>
                            <div className={styles['footer-brand-text']}>
                                Dev<span>Hire</span>
                            </div>
                        </div>
                        <p className={styles['footer-desc']}>
                            Nền tảng công nghệ kết nối việc làm và phát triển sự nghiệp lập trình viên thế hệ mới. Tìm kiếm cơ hội làm việc tại hàng trăm doanh nghiệp công nghệ hàng đầu.
                        </p>
                        <div style={{ display: 'flex', flexDirection: 'column', gap: 8, fontSize: 13, color: '#94a3b8' }}>
                            <div><EnvironmentOutlined style={{ color: '#818cf8', marginRight: 8 }} /> Tầng 8, Tòa nhà Innovation, Cầu Giấy, Hà Nội</div>
                            <div><MailOutlined style={{ color: '#818cf8', marginRight: 8 }} /> contact@devhire.io</div>
                            <div><PhoneOutlined style={{ color: '#818cf8', marginRight: 8 }} /> (+84) 988 123 456</div>
                        </div>
                    </div>

                    {/* For Candidates */}
                    <div className={styles['footer-col']}>
                        <h4>Dành Cho Ứng Viên</h4>
                        <ul>
                            <li><Link to="/job">Tất cả việc làm IT</Link></li>
                            <li><Link to="/company">Top công ty công nghệ</Link></li>
                            <li><a href="#salary">Tra cứu mức lương IT</a></li>
                            <li><a href="#cv-templates">Mẫu CV IT chuyên nghiệp</a></li>
                            <li><a href="#tips">Kinh nghiệm phỏng vấn</a></li>
                        </ul>
                    </div>

                    {/* For Employers */}
                    <div className={styles['footer-col']}>
                        <h4>Nhà Tuyển Dụng</h4>
                        <ul>
                            <li><Link to="/admin">Đăng tin tuyển dụng</Link></li>
                            <li><a href="#pricing">Bảng giá dịch vụ</a></li>
                            <li><a href="#search-cv">Tìm kiếm ứng viên</a></li>
                            <li><a href="#report">Báo cáo thị trường IT 2026</a></li>
                            <li><a href="#partner">Đối tác doanh nghiệp</a></li>
                        </ul>
                    </div>

                    {/* Tech & Ecosystem */}
                    <div className={styles['footer-col']}>
                        <h4>Công Nghệ Phổ Biến</h4>
                        <ul>
                            <li><Link to="/job?skills=REACT.JS">Việc làm React.JS</Link></li>
                            <li><Link to="/job?skills=JAVA,JAVA SPRING">Việc làm Java Spring</Link></li>
                            <li><Link to="/job?skills=NEST.JS">Việc làm NodeJS / NestJS</Link></li>
                            <li><Link to="/job?skills=FRONTEND">Việc làm Frontend Developer</Link></li>
                            <li><Link to="/job?skills=BACKEND">Việc làm Backend Developer</Link></li>
                        </ul>
                    </div>
                </div>

                <div className={styles['footer-bottom']}>
                    <div>
                        &copy; {new Date().getFullYear()} <strong>DevHire Platform</strong>. All rights reserved. Designed with modern aesthetics.
                    </div>
                    <div className={styles['footer-socials']}>
                        <a href="https://github.com" target="_blank" rel="noreferrer" title="GitHub"><GithubOutlined /></a>
                        <a href="https://linkedin.com" target="_blank" rel="noreferrer" title="LinkedIn"><LinkedinOutlined /></a>
                        <a href="https://facebook.com" target="_blank" rel="noreferrer" title="Facebook"><FacebookOutlined /></a>
                        <a href="https://twitter.com" target="_blank" rel="noreferrer" title="Twitter"><TwitterOutlined /></a>
                    </div>
                </div>
            </div>
        </footer>
    );
};

export default Footer;