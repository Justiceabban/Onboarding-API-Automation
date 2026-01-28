# Script to add Allure imports to all test files

$testFiles = @(
    "tests\migration\DataMigrationTest.java",
    "tests\journeysettings\JourneySettingsTest.java",
    "tests\language\LanguageTest.java",
    "tests\feedback\StageFeedbackTest.java",
    "tests\favourite\FavouriteTest.java",
    "tests\checklist\ChecklistTest.java",
    "tests\token\TokenVerificationTest.java",
    "tests\chapter\ChapterTest.java",
    "tests\category\CategoryTest.java",
    "tests\attachment\AttachmentTest.java",
    "tests\cases\CaseTest.java",
    "tests\asset\AssetManagementTest.java",
    "tests\assignment\AssignmentTest.java"
)

# Epic/Feature mapping
$mappings = @{
    "DataMigrationTest" = @{Epic="Data Management"; Feature="Data Migration"}
    "JourneySettingsTest" = @{Epic="Journey Management"; Feature="Journey Settings"}
    "LanguageTest" = @{Epic="Localization"; Feature="Language Management"}
    "StageFeedbackTest" = @{Epic="Journey Management"; Feature="Stage Feedback"}
    "FavouriteTest" = @{Epic="User Management"; Feature="Favourites"}
    "ChecklistTest" = @{Epic="User Management"; Feature="User Checklist"}
    "TokenVerificationTest" = @{Epic="Authentication"; Feature="Token Verification"}
    "ChapterTest" = @{Epic="Content Management"; Feature="Chapter Management"}
    "CategoryTest" = @{Epic="Content Management"; Feature="Categories"}
    "AttachmentTest" = @{Epic="Content Management"; Feature="Attachments"}
    "CaseTest" = @{Epic="Case Management"; Feature="Cases"}
    "AssetManagementTest" = @{Epic="Asset Management"; Feature="Asset Operations"}
    "AssignmentTest" = @{Epic="User Management"; Feature="Assignments"}
}

Write-Host "Files to update with Allure annotations:"
foreach ($file in $testFiles) {
    Write-Host "  - $file"
}
