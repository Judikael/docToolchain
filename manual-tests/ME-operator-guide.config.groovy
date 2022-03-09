outputPath = 'build/ME-operator-guide'

// Path where the docToolchain will search for the input files.
// This path is appended to the docDir property specified in gradle.properties
// or in the command line, and therefore must be relative to it.

inputPath = 'ME-operator-guide';
pdfThemeDir = './docs/pdfTheme'

inputFiles = [
        [file: 'index.adoc', formats: ['html','pdf','docbook','docx']],
        //[file: 'doctoolchain_demo.adoc',       formats: ['html','pdf']],
        //[file: 'arc42-template.adoc',    formats: ['html','pdf']],
        /** inputFiles **/
]

//folders in which asciidoc will find images.
//these will be copied as resources to ./images
imageDirs = [
    'assets',
    /** imageDirs **/
]

// these are directories (dirs) and files which Gradle monitors for a change
// in order to decide if the docs have to be re-build
taskInputsDirs = [
                    "${inputPath}",                 
//                  "${inputPath}/src",
//                  "${inputPath}/images",
                 ]

taskInputsFiles = []

//*****************************************************************************************

//tag::confluenceConfig[]
//Configureation for publishToConfluence
// 'input' is an array of files to upload to Confluence with the ability
//          to configure a different parent page for each file.
//
// Attributes
// - 'file': absolute or relative path to the asciidoc generated html file to be exported
// - 'url': absolute URL to an asciidoc generated html file to be exported
// - 'ancestorName' (optional): the name of the parent page in Confluence as string;
//                             this attribute has priority over ancestorId, but if page with given name doesn't exist,
//                             ancestorId will be used as a fallback
// - 'ancestorId' (optional): the id of the parent page in Confluence as string; leave this empty
//                            if a new parent shall be created in the space
// - 'preambleTitle' (optional): the title of the page containing the preamble (everything
//                            before the first second level heading). Default is 'arc42'
//
// The following four keys can also be used in the global section below
// - 'spaceKey' (optional): page specific variable for the key of the confluence space to write to
// - 'createSubpages' (optional): page specific variable to determine whether ".sect2" sections shall be split from the current page into subpages
// - 'pagePrefix' (optional): page specific variable, the pagePrefix will be a prefix for the page title and it's sub-pages
//                            use this if you only have access to one confluence space but need to store several
//                            pages with the same title - a different pagePrefix will make them unique
// - 'pageSuffix' (optional): same usage as prefix but appended to the title and it's subpages
// only 'file' or 'url' is allowed. If both are given, 'url' is ignored

confluence = [

    input: [
            [ file: "build/ME-operator-guide/html5/index.html" ],
    ],

    // endpoint of the confluenceAPI (REST) to be used
    // to verify the endpoint, add user/current and pate it into your browser
    // you should get a json about your own user
    api: 'https://gaki.atlassian.net/wiki/rest/api/',

    //    Additionally, spaceKey, createSubpages, pagePrefix and pageSuffix can be globally defined here. The assignment in the input array has precedence

    // the key of the confluence space to write to
    spaceKey: 'DTC',
    // ancestorId: '229620',
    // ancestorName: 'Test',

    // the title of the page containing the preamble (everything the first second level heading). Default is 'arc42'
    preambleTitle: "${scmref} - Manuel d'Exploitation",

    // variable to determine whether ".sect2" sections shall be split from the current page into subpages
    createSubpages: false,

    // the pagePrefix will be a prefix for each page title
    // use this if you only have access to one confluence space but need to store several
    // pages with the same title - a different pagePrefix will make them unique
    pagePrefix: '',

    pageSuffix: " [medtc-${scmref}]",

    // HTML Content that will be included with every page published
    // directly after the TOC. If left empty no additional content will be
    // added
    extraPageContent: '<ac:structured-macro ac:name="info"><ac:rich-text-body>Page générée, ne la modifiez pas ! Utilisez les commentaires ou éditez là sous GitLab.</ac:rich-text-body></ac:structured-macro>',

    // enable or disable attachment uploads for local file references
    enableAttachments: true,

    // default attachmentPrefix = attachment - All files to attach will require to be linked inside the document.
    attachmentPrefix: "medtc-${scmref}-",
]
//end::confluenceConfig[]

// Configuration for readGitInfo.gradle
readScmInfo = [:]
readScmInfo.with {
    enable = true
}

// Additionnal asciidoc attributes
docAttributes = [
    revnumber: "${scmref}",
    revdate: "${scmdate}",
]