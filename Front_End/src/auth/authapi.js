export const signup = (user) => {
    return new Promise((resolve) => {
        console.log("Mock Signup Request:", user);
        setTimeout(() => {
            resolve({ message: "Signup successful!" });
        }, 500);
    });
};
export const signin = (user) => {
    return new Promise((resolve) => {
        console.log("Mock Signin Request:", user);
        setTimeout(() => {
            if (user.email === "manager@example.com") {
                resolve({
                    token: "mockToken456",
                    user: { name: "Manager User", email: user.email, role: "manager" },
                });
            } else {
                resolve({
                    token: "mockToken789",
                    user: { name: "Customer User", email: user.email, role: "customer" },
                });
            }
        }, 500);
    });
};

export const signout= next=>{
    if(typeof window!== "undefined"){
        localStorage.removeItem("jwt")
        next();
        console.log("Signout successfully");
    }
}

export const authenticate = (data, next) => {
    if (typeof window !== "undefined") {
        localStorage.setItem("jwt", JSON.stringify(data));
        next();
    }
};

export const isAuthenticated = () => {
    if (typeof window === "undefined") {
        return false;
    }
    if (localStorage.getItem("jwt")) {
        return JSON.parse(localStorage.getItem("jwt"));
    }
    return false;
};

