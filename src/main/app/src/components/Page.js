import React from 'react'

const Page = ({header, children}) =>
    <div className="App">
        <header className="App-header">
            <h1 className="App-title">Trade Monitor Dashboard</h1>
            <span className="App-subtitle">powered by <span class="sr-only">hazelcast</span>&nbsp;<img className="hazelcastLogo" src="/images/hazelcast_logo.png" srcset="" alt=""/></span>
        </header>
        <main>
            {children}
        </main>
    </div>

export default Page
