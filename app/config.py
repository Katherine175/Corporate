"""
Smart Corporate Resource & Financial Optimizer
Application Configuration
"""

from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    app_name: str = "Smart Corporate Resource & Financial Optimizer"
    app_version: str = "1.0.0"
    debug: bool = False

    # Security
    secret_key: str = "change-me-in-production"
    access_token_expire_minutes: int = 60
    algorithm: str = "HS256"

    # Database
    database_url: str = "sqlite:///./corporate.db"

    class Config:
        env_file = ".env"


settings = Settings()
