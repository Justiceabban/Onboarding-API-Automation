# 🚀 GitLab CI/CD Integration with Allure Reports

## ✅ Files Created

I've created **2 GitLab CI configuration files** for your project:

1. **`.gitlab-ci.yml`** - Full-featured pipeline (RECOMMENDED)
2. **`.gitlab-ci-simple.yml`** - Simplified version (backup)

---

## 📋 What the Pipeline Does

### Main Pipeline (`.gitlab-ci.yml`):

```
Stage 1: BUILD
  └─ Compiles Java code and test classes
  └─ Creates target/ directory
  └─ Caches Maven dependencies

Stage 2: TEST
  └─ Runs all API tests
  └─ Generates allure-results/
  └─ Creates JUnit XML reports
  └─ Allows failures (continues even if tests fail)

Stage 3: REPORT
  └─ Generates beautiful Allure HTML report
  └─ Creates allure-report/ directory
  └─ Makes report downloadable as artifact

Stage 4: PAGES (Optional)
  └─ Deploys report to GitLab Pages
  └─ Makes report publicly accessible via URL
```

---

## 🎯 Pipeline Features

### ✅ Test Execution:
- Runs all 237+ test scenarios
- Uses Maven 3.8.6 with OpenJDK 11
- Caches dependencies for faster builds
- Continues even if tests fail

### ✅ Allure Report Generation:
- Automatically generates HTML report
- Includes all test results, HTTP logs, screenshots
- Beautiful interactive UI
- Downloadable as artifact

### ✅ GitLab Integration:
- Test results visible in GitLab Merge Requests
- JUnit reports integrated with GitLab UI
- Allure report downloadable from pipeline artifacts
- Optional deployment to GitLab Pages

### ✅ Artifacts:
- **allure-results/** - Raw test data (7 days)
- **allure-report/** - HTML report (30 days)
- **surefire-reports/** - JUnit XML (7 days)

---

## 🚀 How to Use

### Step 1: Push to GitLab

```bash
# Add the files to git
git add .gitlab-ci.yml
git add .gitlab-ci-simple.yml

# Commit
git commit -m "Add GitLab CI/CD pipeline with Allure reporting"

# Push to GitLab
git push origin main
```

### Step 2: Pipeline Runs Automatically

GitLab will automatically:
1. Detect `.gitlab-ci.yml`
2. Start the pipeline
3. Run all stages
4. Generate reports

### Step 3: View Results

#### Option A: Download Allure Report from Artifacts

1. Go to **CI/CD → Pipelines**
2. Click on the latest pipeline
3. Click on **allure-report** job
4. On the right side, click **Browse** under Artifacts
5. Click **Download** or view directly

#### Option B: View on GitLab Pages (Public URL)

If Pages stage succeeds:
1. Go to **Settings → Pages**
2. Copy the Pages URL (e.g., `https://username.gitlab.io/project-name`)
3. Open in browser
4. View beautiful Allure report!

#### Option C: View Test Results in GitLab UI

1. Go to **CI/CD → Pipelines**
2. Click on pipeline
3. See test results summary in GitLab native UI
4. View JUnit test reports

---

## 📊 Pipeline Configuration Details

### Variables:
```yaml
MAVEN_OPTS: "-Dmaven.repo.local=$CI_PROJECT_DIR/.m2/repository"
MAVEN_CLI_OPTS: "--batch-mode --errors --fail-at-end --show-version"
ALLURE_VERSION: "2.25.0"
```

### Docker Images Used:
- **Build & Test:** `maven:3.8.6-openjdk-11`
- **Allure Report:** `frankescobar/allure-docker-service:latest`

### Caching:
```yaml
cache:
  paths:
    - .m2/repository  # Maven dependencies
    - target/         # Compiled classes
```

This speeds up subsequent pipeline runs!

---

## 🔧 Customization Options

### Run Specific Tests:

Edit `.gitlab-ci.yml` test stage:
```yaml
script:
  - mvn test -Dtest=UserManagementTest
```

### Change Report Retention:

```yaml
artifacts:
  expire_in: 90 days  # Keep reports for 90 days
```

### Run on Specific Branches:

```yaml
only:
  - main
  - develop
  - /^release-.*$/  # All release branches
```

### Add Environment Variables:

```yaml
variables:
  API_BASE_URL: "https://api.example.com"
  ENVIRONMENT: "test"
```

Then use in tests via `System.getProperty("API_BASE_URL")`

---

## 🎯 Viewing Allure Report in GitLab

### Method 1: Artifact Browser (Easiest)

1. **Go to Pipeline:**
   - CI/CD → Pipelines
   - Click latest pipeline

2. **Open Report Job:**
   - Click `allure-report` job
   - Wait for completion

3. **Browse Artifacts:**
   - Right side: Click **Browse** button
   - Navigate to `allure-report/`
   - Click `index.html`
   - View report directly in browser!

### Method 2: Download and View Locally

1. **Download Artifact:**
   - CI/CD → Pipelines
   - Click pipeline → allure-report job
   - Click **Download** button

2. **Extract and View:**
   - Extract `allure-report.zip`
   - Open `allure-report/index.html` in browser
   - Enjoy the report!

### Method 3: GitLab Pages (Public URL)

**Setup Required:**
1. Go to **Settings → General → Visibility**
2. Enable **Pages** (if not already enabled)

**After pipeline runs:**
1. Go to **Settings → Pages**
2. Copy the URL (e.g., `https://yourusername.gitlab.io/apiTasTesting3`)
3. Open in browser
4. Report is live and accessible!

**Note:** Pages deploy only on main/master/develop branches by default.

---

## 📋 Pipeline Status Badge

Add this to your README.md to show pipeline status:

```markdown
[![pipeline status](https://gitlab.com/yourusername/apiTasTesting3/badges/main/pipeline.svg)](https://gitlab.com/yourusername/apiTasTesting3/-/commits/main)
```

---

## 🐛 Troubleshooting

### Issue: Pipeline fails at test stage
**Cause:** API server might be down (503 errors)
**Solution:** This is OK! Tests will fail but report will still be generated.
```yaml
allow_failure: true  # Already set in test stage
```

### Issue: Allure report generation fails
**Cause:** No allure-results directory
**Solution:** Check if tests actually ran:
```yaml
artifacts:
  when: always  # Upload artifacts even if job fails
```

### Issue: Pages deployment fails
**Cause:** Pages might be disabled
**Solution:** 
1. Go to Settings → General → Visibility
2. Enable Pages
3. Re-run pipeline

### Issue: "Out of memory" errors
**Solution:** Increase memory in variables:
```yaml
MAVEN_OPTS: "-Dmaven.repo.local=$CI_PROJECT_DIR/.m2/repository -Xmx2048m"
```

### Issue: Cache not working
**Solution:** Clear pipeline cache:
1. CI/CD → Pipelines
2. Click "Clear runner caches" button
3. Re-run pipeline

---

## 📊 Expected Pipeline Duration

| Stage | Duration | Notes |
|-------|----------|-------|
| Build | 1-2 min | First run: 3-5 min (downloads deps) |
| Test | 3-5 min | Depends on API response time |
| Report | 30 sec | Generates HTML from JSON |
| Pages | 10 sec | Copies files to public/ |
| **Total** | **5-8 min** | First run: 8-12 min |

---

## ✅ Verification Checklist

After pushing to GitLab:

- [ ] Pipeline starts automatically
- [ ] Build stage passes (green)
- [ ] Test stage completes (may be yellow if tests fail)
- [ ] Report stage passes (green)
- [ ] Artifacts are available for download
- [ ] Report can be viewed in artifact browser
- [ ] (Optional) Pages deployment succeeds
- [ ] (Optional) Report accessible via Pages URL

---

## 🎉 What You Get

### In GitLab UI:
✅ Pipeline status for every commit  
✅ Test results summary in MR  
✅ JUnit test reports  
✅ Downloadable Allure reports  
✅ Test history and trends  

### In Allure Report:
✅ Beautiful interactive dashboard  
✅ Test organization by Epic/Feature/Story  
✅ HTTP request/response logs  
✅ Execution timeline  
✅ Flaky test detection  
✅ Historical trends  
✅ Charts and graphs  

### Accessible Via:
✅ GitLab artifact browser  
✅ Direct download (ZIP)  
✅ GitLab Pages (public URL)  
✅ Can be shared with team  

---

## 🔄 CI/CD Workflow

```
Developer pushes code
    ↓
GitLab detects .gitlab-ci.yml
    ↓
Pipeline starts automatically
    ↓
Stage 1: Build (compile code)
    ↓
Stage 2: Test (run 237+ tests)
    ↓
Stage 3: Generate Allure report
    ↓
Stage 4: Deploy to Pages (optional)
    ↓
Report available in GitLab
    ↓
Team views results
```

---

## 📚 Additional Resources

### GitLab CI/CD Documentation:
- Pipeline configuration: https://docs.gitlab.com/ee/ci/yaml/
- GitLab Pages: https://docs.gitlab.com/ee/user/project/pages/
- Artifacts: https://docs.gitlab.com/ee/ci/pipelines/job_artifacts.html

### Allure Documentation:
- Allure Report: https://docs.qameta.io/allure/
- Allure TestNG: https://docs.qameta.io/allure/#_testng

---

## 🎯 Quick Start Commands

```bash
# 1. Add CI files to repository
git add .gitlab-ci.yml README_GITLAB_CI.md

# 2. Commit
git commit -m "Add GitLab CI/CD pipeline with Allure reporting"

# 3. Push to GitLab
git push origin main

# 4. Go to GitLab and watch pipeline run!
# CI/CD → Pipelines → Click latest pipeline

# 5. View report when done
# Click "allure-report" job → Browse artifacts → index.html
```

---

## 🎊 Summary

**Files Created:**
- ✅ `.gitlab-ci.yml` - Full pipeline configuration
- ✅ `.gitlab-ci-simple.yml` - Simplified backup
- ✅ `README_GITLAB_CI.md` - This documentation

**What Happens:**
- ✅ Automatic test execution on every push
- ✅ Beautiful Allure reports generated
- ✅ Reports accessible in GitLab
- ✅ Optional public URL via Pages
- ✅ Team collaboration ready

**Next Steps:**
1. Push files to GitLab
2. Watch pipeline run
3. View Allure report
4. Share with team! 🎉

---

**Your GitLab CI/CD pipeline is ready! Just push to GitLab and watch the magic happen!** ✅
