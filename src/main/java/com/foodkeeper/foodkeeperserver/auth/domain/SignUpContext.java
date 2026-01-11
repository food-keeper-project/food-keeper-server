package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.auth.domain.enums.MemberRole;
import com.foodkeeper.foodkeeperserver.member.domain.*;
import com.foodkeeper.foodkeeperserver.member.domain.enums.Gender;
import com.foodkeeper.foodkeeperserver.member.domain.enums.SignUpType;

import java.util.List;

public record SignUpContext(LocalAccount account,
                            Password password,
                            Email email,
                            Nickname nickname,
                            Gender gender,
                            IpAddress ipAddress) {

    public NewLocalMember toNewLocalMember(EncodedPassword encodedPassword) {
        NewMember newMember = NewMember.builder()
                .email(email)
                .nickname(nickname)
                .imageUrl(ProfileImageUrl.empty())
                .signUpType(SignUpType.LOCAL)
                .ipAddress(ipAddress)
                .memberRoles(new MemberRoles(List.of(MemberRole.ROLE_USER)))
                .build();
        return NewLocalMember.builder()
                .member(newMember)
                .account(account)
                .password(encodedPassword)
                .build();
    }

    public String getPassword() {
        return password.password();
    }

    public String getEmail() {
        return email.email();
    }

    public static SignUpContextBuilder builder() {
        return new SignUpContextBuilder();
    }

    public static class SignUpContextBuilder {
        private String account;
        private String password;
        private String email;
        private String nickname;
        private Gender gender;
        private String ipAddress;

        public SignUpContextBuilder account(String account) {
            this.account = account;
            return this;
        }

        public SignUpContextBuilder password(String password) {
            this.password = password;
            return this;
        }

        public SignUpContextBuilder email(String email) {
            this.email = email;
            return this;
        }

        public SignUpContextBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public SignUpContextBuilder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public SignUpContextBuilder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public SignUpContext build() {
            return new SignUpContext(new LocalAccount(account),
                    new Password(password),
                    new Email(email),
                    new Nickname(nickname),
                    gender,
                    new IpAddress(ipAddress)
            );
        }
    }
}
