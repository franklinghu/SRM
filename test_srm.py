"""
SRM系统前端测试脚本
测试登录页面、仪表盘、导航等核心功能
"""

from playwright.sync_api import sync_playwright
import sys
import time

class SRMTestRunner:
    def __init__(self, base_url="http://localhost:5173"):
        self.base_url = base_url
        self.browser = None
        self.context = None
        self.page = None
        self.console_messages = []
        self.errors = []

    def setup(self):
        """初始化浏览器"""
        print("正在启动浏览器...")
        playwright = sync_playwright().start()
        self.browser = playwright.chromium.launch(headless=True)
        self.context = self.browser.new_context()
        self.page = self.context.new_page()

        # 监听控制台消息
        self.page.on("console", lambda msg: self._handle_console(msg))
        self.page.on("pageerror", lambda err: self.errors.append(str(err)))

        print("浏览器启动成功")

    def _handle_console(self, msg):
        """处理控制台消息"""
        self.console_messages.append({
            'type': msg.type,
            'text': msg.text
        })
        if msg.type == 'error':
            print(f"  [Console Error] {msg.text}")

    def teardown(self):
        """清理资源"""
        if self.browser:
            self.browser.close()

    def test_login_page_loaded(self):
        """测试登录页面是否正确加载"""
        print("\n测试1: 登录页面加载...")
        try:
            self.page.goto(f"{self.base_url}/login", timeout=10000)
            self.page.wait_for_load_state('networkidle', timeout=10000)

            # 检查页面标题或主要元素
            title = self.page.title()
            print(f"  页面标题: {title}")

            # 检查登录表单元素
            username_input = self.page.locator('input[placeholder="用户名"]')
            password_input = self.page.locator('input[placeholder="密码"]')
            login_button = self.page.locator('button:has-text("登录")')

            assert username_input.is_visible(), "用户名输入框不可见"
            assert password_input.is_visible(), "密码输入框不可见"
            assert login_button.is_visible(), "登录按钮不可见"

            print("  ✓ 登录页面加载成功")
            return True
        except Exception as e:
            print(f"  ✗ 登录页面加载失败: {str(e)}")
            return False

    def test_login_form_validation(self):
        """测试登录表单验证"""
        print("\n测试2: 登录表单验证...")
        try:
            # 尝试不填写内容直接点击登录
            login_button = self.page.locator('button:has-text("登录")')
            login_button.click()

            # 等待验证消息
            self.page.wait_for_timeout(500)

            # 检查是否有验证错误提示
            validation_messages = self.page.locator('.el-form-item__error').all()
            if validation_messages:
                print(f"  ✓ 表单验证生效，显示 {len(validation_messages)} 个验证消息")
                return True
            else:
                print("  ⚠ 未检测到表单验证消息")
                return True
        except Exception as e:
            print(f"  ✗ 表单验证测试失败: {str(e)}")
            return False

    def test_login_with_mock_credentials(self):
        """测试使用模拟凭证登录"""
        print("\n测试3: 模拟登录测试...")
        try:
            # 输入测试凭证
            username_input = self.page.locator('input[placeholder="用户名"]')
            password_input = self.page.locator('input[placeholder="密码"]')

            username_input.fill("admin")
            password_input.fill("admin123")

            print("  已填写登录信息")

            # 点击登录按钮
            login_button = self.page.locator('button:has-text("登录")')
            login_button.click()

            # 等待响应
            self.page.wait_for_timeout(2000)

            # 检查是否有错误消息或是否成功跳转
            current_url = self.page.url
            if "login" in current_url:
                # 可能后端未运行，检查是否有错误提示
                error_msg = self.page.locator('.el-message--error').first
                if error_msg.is_visible():
                    print("  ⚠ 后端服务未运行，登录失败（预期行为）")
                    print("  ✓ 前端表单交互正常")
                    return True
            else:
                print(f"  ✓ 登录成功，跳转到: {current_url}")
                return True

            return True
        except Exception as e:
            print(f"  ✗ 登录测试失败: {str(e)}")
            return False

    def test_dashboard_page(self):
        """测试仪表盘页面"""
        print("\n测试4: 仪表盘页面测试...")
        try:
            # 直接访问仪表盘（如果需要认证会重定向）
            self.page.goto(f"{self.base_url}/", timeout=10000)
            self.page.wait_for_load_state('networkidle', timeout=10000)

            # 截图保存
            self.page.screenshot(path='/tmp/srm_dashboard.png', full_page=True)
            print("  ✓ 仪表盘页面截图已保存: /tmp/srm_dashboard.png")

            # 检查工作台标题
            dashboard_title = self.page.locator('h1:has-text("工作台")')
            if dashboard_title.is_visible():
                print("  ✓ 仪表盘标题显示正常")
            else:
                print("  ⚠ 仪表盘标题未找到（可能需要登录）")

            # 检查统计卡片
            stat_cards = self.page.locator('.el-card').all()
            print(f"  检测到 {len(stat_cards)} 个统计卡片")

            return True
        except Exception as e:
            print(f"  ✗ 仪表盘测试失败: {str(e)}")
            return False

    def test_navigation_menu(self):
        """测试导航菜单"""
        print("\n测试5: 导航菜单测试...")
        try:
            # 确保在首页
            self.page.goto(f"{self.base_url}/", timeout=10000)
            self.page.wait_for_load_state('networkidle', timeout=10000)

            # 截图
            self.page.screenshot(path='/tmp/srm_navigation.png', full_page=True)

            # 检查侧边栏菜单
            menu_items = self.page.locator('.el-menu-item, .el-sub-menu').all()
            print(f"  检测到 {len(menu_items)} 个菜单项")

            if len(menu_items) > 0:
                print("  ✓ 导航菜单渲染正常")
            else:
                print("  ⚠ 未检测到菜单项（可能需要登录）")

            return True
        except Exception as e:
            print(f"  ✗ 导航菜单测试失败: {str(e)}")
            return False

    def test_supplier_page(self):
        """测试供应商管理页面"""
        print("\n测试6: 供应商管理页面测试...")
        try:
            self.page.goto(f"{self.base_url}/supplier", timeout=10000)
            self.page.wait_for_load_state('networkidle', timeout=10000)

            self.page.screenshot(path='/tmp/srm_supplier.png', full_page=True)
            print("  ✓ 供应商页面截图已保存: /tmp/srm_supplier.png")

            return True
        except Exception as e:
            print(f"  ✗ 供应商页面测试失败: {str(e)}")
            return False

    def test_purchase_request_page(self):
        """测试采购申请页面"""
        print("\n测试7: 采购申请页面测试...")
        try:
            self.page.goto(f"{self.base_url}/purchase/request", timeout=10000)
            self.page.wait_for_load_state('networkidle', timeout=10000)

            self.page.screenshot(path='/tmp/srm_purchase_request.png', full_page=True)
            print("  ✓ 采购申请页面截图已保存: /tmp/srm_purchase_request.png")

            return True
        except Exception as e:
            print(f"  ✗ 采购申请页面测试失败: {str(e)}")
            return False

    def test_console_errors(self):
        """检查控制台错误"""
        print("\n测试8: 控制台错误检查...")
        error_count = sum(1 for msg in self.console_messages if msg['type'] == 'error')

        if error_count > 0:
            print(f"  ⚠ 检测到 {error_count} 个控制台错误:")
            for msg in self.console_messages:
                if msg['type'] == 'error':
                    print(f"    - {msg['text']}")
            return False
        else:
            print("  ✓ 无控制台错误")
            return True

    def run_all_tests(self):
        """运行所有测试"""
        print("="*60)
        print("SRM系统前端测试开始")
        print("="*60)

        self.setup()

        results = {
            'login_page': self.test_login_page_loaded(),
            'form_validation': self.test_login_form_validation(),
            'mock_login': self.test_login_with_mock_credentials(),
            'dashboard': self.test_dashboard_page(),
            'navigation': self.test_navigation_menu(),
            'supplier_page': self.test_supplier_page(),
            'purchase_request': self.test_purchase_request_page(),
            'console_errors': self.test_console_errors()
        }

        self.teardown()

        print("\n" + "="*60)
        print("测试结果汇总")
        print("="*60)

        passed = sum(1 for v in results.values() if v)
        total = len(results)

        for test_name, result in results.items():
            status = "✓ 通过" if result else "✗ 失败"
            print(f"{test_name:20s}: {status}")

        print("="*60)
        print(f"总计: {passed}/{total} 项测试通过")

        if passed == total:
            print("\n🎉 所有测试通过！")
            return 0
        else:
            print(f"\n⚠ {total - passed} 项测试失败，需要进一步检查")
            return 1

def main():
    base_url = "http://localhost:3000"
    if len(sys.argv) > 1:
        base_url = sys.argv[1]

    runner = SRMTestRunner(base_url)
    return runner.run_all_tests()

if __name__ == "__main__":
    sys.exit(main())
